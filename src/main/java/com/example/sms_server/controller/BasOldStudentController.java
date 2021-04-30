package com.example.sms_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasOldStudent;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.mapper.BasOldStudentMapper;
import com.example.sms_server.service.BasOldStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oldStudent")
@Slf4j
public class BasOldStudentController {

    @Autowired
    BasOldStudentService basOldStudentService;



    @PostMapping("/findAll")
    public ResponseResult findAll(@RequestBody JSONObject jsonObject) {
        String str = jsonObject.getString("oldStudent");
        Object basOldStudentStr = net.sf.json.JSONObject.fromObject(str);
        String oldStudentstr = basOldStudentStr.toString();
        ///basStudent其实是被封装成了一个查询条件
        BasOldStudent basOldStudent = JSONObject.parseObject(oldStudentstr, BasOldStudent.class);
        log.info("basStudentStr是" + basOldStudentStr);
        PageData<BasOldStudent> data;
        data = basOldStudentService.findPage(basOldStudent,
                Long.parseLong(jsonObject.getString("page")),
                Long.parseLong(jsonObject.getString("limit")));
        //对分页查询结果进行处理
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(data);
        return responseResult;
    }
}
