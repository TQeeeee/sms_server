package com.example.sms_server.controller;


import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasMajor;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.entity.display.DisplayMajor;
import com.example.sms_server.service.BasMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/major")
public class BasMajorController {
    @Autowired
    BasMajorService basMajorService;
    @GetMapping("/findAll")
    public ResponseResult findAll(){
        ResponseResult responseResult = new ResponseResult();
        PageData<BasMajor> data =
                new PageData<BasMajor>(Integer.toUnsignedLong(basMajorService.count()),basMajorService.list());
        responseResult.setData(data);
        return responseResult;
    }
    @GetMapping("/findAllDisplay")
    public ResponseResult findAllDisplay(){
        ResponseResult responseResult = new ResponseResult();
        List<DisplayMajor> displayMajorList = basMajorService.findAllDisplayMajor();
        responseResult.setData(displayMajorList);
        return responseResult;
    }
    //添加专业
    @PostMapping("/create")
    public ResponseResult addBasMajor(@RequestBody BasMajor basMajor) {
        ResponseResult responseResult = new ResponseResult();
        try {
            basMajorService.save(basMajor);

        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("插入失败！");
        }
        return responseResult;
    }
    @PutMapping("/update")
    public ResponseResult update(@RequestBody BasMajor basMajor){
        ResponseResult responseResult = new ResponseResult();
        try {
            basMajorService.updateById(basMajor);
        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("更新失败！");
        }
        return responseResult;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String majorId){
        ResponseResult responseResult = new ResponseResult();
        try{
            basMajorService.removeById(majorId);
        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("删除失败！");
        }
        return responseResult;
    }

}
