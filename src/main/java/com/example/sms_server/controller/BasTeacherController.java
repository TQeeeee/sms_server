package com.example.sms_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasTeacher;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.service.BasTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 与教师相关的各类请求...前端页面是根据角色跳转到对应的菜单项
 * 点击查看个人信息的菜单项后，向后端发请求，前端根据用户角色判断显示内容，并且查找对应的不同的表（参数不同即可）
 * 效果：查看个人信息界面--前端根据用户不同角色，调用不同的接口---后端只需接受和业务逻辑相关
 * controller只负责将请求和参数派发到service执行，并接受执行结果
 */

@RestController
@RequestMapping("/teacher")
@Slf4j
public class BasTeacherController {

    @Autowired
    BasTeacherService basTeacherService;
    //获取用户基本信息
    @PostMapping("/create")
    public ResponseResult create(@RequestBody BasTeacher basTeacher){
        basTeacher.setStart(new Date(System.currentTimeMillis()));
        ResponseResult result = new ResponseResult();
        try {
            //直接将教师插入教师表即可
           basTeacherService.addTeacher(basTeacher);
        } catch (Exception e){
            e.printStackTrace();
            result.setData("插入失败！");
        }
        return result;
    }
    @GetMapping("/get/{id}")
    public ResponseResult get(@PathVariable("id") String teacherId){
        ResponseResult responseResult = new ResponseResult();
        //先查询，再根据审核结果决定是否要返回给前端
        BasTeacher basTeacher = basTeacherService.getById(teacherId);
        if(basTeacher.getValided() == 1){
            responseResult.setData(basTeacher);
        }else{
            responseResult.setCode(20007);
            responseResult.setData("尚未审核成功");
        }
        return responseResult;
    }
    @GetMapping("/getAll")
    public ResponseResult getAll(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(basTeacherService.list());
        return responseResult;
    }



    @PostMapping("/findAll")
    public ResponseResult findAll(@RequestBody JSONObject jsonObject) {
        String str = jsonObject.getString("teacher");
        Object basTeacherStr = net.sf.json.JSONObject.fromObject(str);
        String teacherstr = basTeacherStr.toString();
        ///basTeacher其实是被封装成了一个查询条件
        BasTeacher basTeacher = JSONObject.parseObject(teacherstr, BasTeacher.class);
        log.info("basteacherstr是" + basTeacher);
        PageData<BasTeacher> data;
        ResponseResult responseResult = new ResponseResult();
        String studentName = jsonObject.getString("studentName");
        if(studentName == null){
            try {
                data = basTeacherService.findPage(basTeacher,
                        Long.parseLong(jsonObject.getString("page")),
                        Long.parseLong(jsonObject.getString("limit")));
                responseResult.setData(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseResult.setCode(20007);
                responseResult.setData("查询失败！");
            }
        }else {
            try{
                data = basTeacherService.findPageByStudentName(studentName,
                        Long.parseLong(jsonObject.getString("page")),
                        Long.parseLong(jsonObject.getString("limit")));
                responseResult.setData(data);
            } catch (Exception e){
                e.printStackTrace();
                responseResult.setCode(20007);
            }
        }


        return responseResult;
    }




    @PutMapping("/update")
    public ResponseResult update(@RequestBody BasTeacher basTeacher){
        ResponseResult responseResult = new ResponseResult();
        log.info(basTeacher.toString());
        try {
            basTeacherService.updateById(basTeacher);
        } catch (Exception e){
            responseResult.setCode(20007);
            responseResult.setData("更新失败");
        }
        return responseResult;
    }





}
