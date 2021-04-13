package com.example.sms_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.service.BasUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class BasUserController {
    @Autowired
    BasUserService basUserService;

    @ResponseBody
    @GetMapping("/get")
    public BasUser getBasUserById(@RequestParam("user_id") String user_id){

        return basUserService.getById(user_id);

    }
    @ResponseBody
    @GetMapping("/findAll")
    public List<BasUser> getAll(){
        return basUserService.list();
    }

    //添加用户-----单条记录
    @PostMapping("/create")
    public ResponseResult addBasUser(@RequestBody BasUser basUser) {
        ResponseResult responseResult = new ResponseResult();
        basUserService.addUser(basUser);
        return responseResult;

    }

    //添加用户 -----批量插入
    @PostMapping("/import")
    public ResponseResult importBasUsers(@RequestParam("importData") String importData) {
        log.info(importData);
        return null;
    }





}
