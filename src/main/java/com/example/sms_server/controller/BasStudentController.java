package com.example.sms_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasStudent;
import com.example.sms_server.entity.BasTeacher;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.service.BasClassService;
import com.example.sms_server.service.BasStudentService;
import com.example.sms_server.service.BasTeacherService;
import com.example.sms_server.utils.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/student")
@Slf4j
public class BasStudentController {
    @Autowired
    BasStudentService basStudentService;
    @Autowired
    BasTeacherService basTeacherService;
    @Autowired
    BasClassService basClassService;


    @PostMapping("/create")
    public ResponseResult create(@RequestBody BasStudent basStudent) {
        ResponseResult result = new ResponseResult();
        try {
            basStudentService.addStudent(basStudent);
            //如果basStudent插入失败，那么basUser也要更新失败
        } catch (RuntimeException e) {
            e.printStackTrace();
            result.setData("插入失败！");
        }
        return result;
    }
    /**
     * 根据studentId获取某位学生信息
     *
     */
    @GetMapping("/get/{id}")
    public ResponseResult getById(@PathVariable("id") String studentId){
        ResponseResult responseResult = new ResponseResult();
        BasStudent basStudent;
        try {
            basStudent = basStudentService.getById(studentId);
            if(basStudent.getConfirmed() == 1)
            responseResult.setData(basStudent);
            else {
                responseResult.setCode(20007);
                responseResult.setData("尚未审核");
            }
        } catch (Exception e){
            responseResult.setData("查找失败");
        }
        return responseResult;
    }



    /**
     * 根据teacherId,classId,majorId，分页...等等条件查询获取所有学生信息
     */
    @PostMapping("/findAll")
    public ResponseResult findAll(@RequestBody JSONObject jsonObject) {
        String str = jsonObject.getString("student");
        Object basStudentStr = net.sf.json.JSONObject.fromObject(str);
        String studentstr = basStudentStr.toString();
        ///basStudent其实是被封装成了一个查询条件
        BasStudent basStudent = JSONObject.parseObject(studentstr, BasStudent.class);
        log.info("basStudentStr是" + basStudentStr);
        PageData<BasStudent> data;

        data = basStudentService.findPage(basStudent, jsonObject.getString("teacherId"),
                Long.parseLong(jsonObject.getString("page")),
                Long.parseLong(jsonObject.getString("limit")));
        //对分页查询结果进行处理
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 根据studentId更改学生信息---完成学生毕业，和学生注册审核(前端必须带着用户的原本信息返回来)
     */
    @PutMapping("/update")
    public ResponseResult update(@RequestBody BasStudent basStudent) {
        basStudentService.updateById(basStudent);
        return new ResponseResult();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String studentId) {
        ResponseResult responseResult = new ResponseResult();
        try {
            basStudentService.deleteStudent(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(20007);
            responseResult.setData("学生删除失败");
        }
        return responseResult;
    }



}
