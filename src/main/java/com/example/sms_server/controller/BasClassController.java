package com.example.sms_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasClass;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.entity.display.DisplayClass;
import com.example.sms_server.service.BasClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.util.JAXBSource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/class")
public class BasClassController {
    @Autowired
    BasClassService basClassService;

    /**
     * 根据教师ID获取班级
     *
     */
    @GetMapping("/get/{id}")
    public ResponseResult getByTeacherId(@PathVariable("id") String teacherId){
        QueryWrapper<BasClass> basClassQueryWrapper = new QueryWrapper<>();
        basClassQueryWrapper.eq("teacher_id",teacherId);
        List<BasClass> basClassList = basClassService.list(basClassQueryWrapper);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(basClassList);
        return responseResult;
    }


    //创建班级
    @PostMapping("/create")
    public ResponseResult addBasUser(@RequestBody BasClass basClass) {
        ResponseResult responseResult = new ResponseResult();
        try {
            basClassService.save(basClass);

        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("插入失败！");
        }
        return responseResult;
    }
    @PutMapping("/update")
    public ResponseResult update(@RequestBody BasClass basClass){
        ResponseResult responseResult = new ResponseResult();
        try {
            basClassService.updateById(basClass);
        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("更新失败！");
        }
        return responseResult;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String classId){
        ResponseResult responseResult = new ResponseResult();
        try{
            basClassService.removeById(classId);
        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("删除失败！");
        }
        return responseResult;
    }
    //更新班级
    //删除班级



    //获取全部的展示的班级
    @PostMapping("/findAllDisplay")
    public ResponseResult findAllDisplay(@RequestBody JSONObject jsonObject){
        String teacherName = jsonObject.getString("teacherName");
        String page = jsonObject.getString("page");
        String limit = jsonObject.getString("limit");
        ResponseResult responseResult = new ResponseResult();
        PageData<DisplayClass> displayPage;

        if (teacherName != null) {
            //要按照教师名进行过滤
            displayPage = basClassService.findAllDisplayClassPageByTeacherName(teacherName
                    , Long.parseLong(page), Long.parseLong(limit));
        } else {
            displayPage = basClassService.findAllDisPlayClassPage(
                    Long.parseLong(page), Long.parseLong(limit));

        }
        responseResult.setData(displayPage);
        return responseResult;
    }



}
