package com.example.sms_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.entity.TempPrize;
import com.example.sms_server.entity.display.DisplayPrize;
import com.example.sms_server.mapper.DisplayPrize2TempPrizeMapper;
import com.example.sms_server.service.DisplayPrizeService;
import com.example.sms_server.service.TempPrizeService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;

@RestController
@RequestMapping("/prize")
public class TempPrizeController {
    @Autowired
    TempPrizeService tempPrizeService;
    @Autowired
    DisplayPrizeService displayPrizeService;

    // 涉及到自增的问题
    @PostMapping("/create")
    public ResponseResult createTempPrize(@RequestBody JSONObject jsonObject){
        ResponseResult responseResult = new ResponseResult();
        TempPrize tempPrize = new TempPrize();
        tempPrize.setStudentId(jsonObject.getString("studentId"));
        tempPrize.setPrizeRecord(jsonObject.getString("prizeRecord"));
        tempPrizeService.save(tempPrize);
        return responseResult;
    }

    @PutMapping("/update")
    public ResponseResult update(@RequestBody DisplayPrize displayPrize) {
        DisplayPrize2TempPrizeMapper d2tMapper = Mappers.getMapper(DisplayPrize2TempPrizeMapper.class);
        TempPrize tempPrize = d2tMapper.displayPrize2TempPrize(displayPrize);
        // 这里只需对tempPrize表进行操作，管理员的审核操作用另一个
        tempPrizeService.updateById(tempPrize);
        return new ResponseResult();
    }
    @PutMapping("/confirm/{id}")
    public ResponseResult confirmTemp(@PathVariable("id") String recordId){
        ResponseResult responseResult = new ResponseResult();
        try {
            tempPrizeService.confirmTempById(recordId);
        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("更新失败！");
        }
        return responseResult;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String recordId) {
        ResponseResult responseResult = new ResponseResult();
        try {
            tempPrizeService.removeById(recordId);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(20007);
            responseResult.setData("记录删除失败");
        }
        return responseResult;
    }




    @PostMapping("/findAll")
    public ResponseResult findAllDisPlay(@RequestBody JSONObject jsonObject){
        QueryWrapper<DisplayPrize> displayPrizeQueryWrapper = new QueryWrapper<>();
        if(jsonObject.getString("teacherId") != null){
            displayPrizeQueryWrapper.eq("teacher_id",jsonObject.getString("teacherId"));
        }
        if(jsonObject.getString("teacherName") != null){
            displayPrizeQueryWrapper.like("teacher_name",jsonObject.getString("teacherName"));
        }
        if(jsonObject.getString("studentName") != null){
            displayPrizeQueryWrapper.like("student_name",jsonObject.getString("studentName"));
        }
        if(jsonObject.getString("studentId") != null){
            displayPrizeQueryWrapper.eq("student_id",jsonObject.getString("studentId"));
        }
        Page<DisplayPrize> page1 = new Page<>(Long.parseLong(jsonObject.getString("page")),
                Long.parseLong(jsonObject.getString("limit")));
        IPage<DisplayPrize> iPage = displayPrizeService.page(page1,displayPrizeQueryWrapper);
        PageData<DisplayPrize> pageData = new PageData<DisplayPrize>(
                iPage.getTotal(),iPage.getRecords());
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(pageData);
        return responseResult;
    }
}
