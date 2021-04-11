package com.example.sms_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.service.BasUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
public class BasUserController {
    @Autowired
    BasUserService basUserService;

    @ResponseBody
    @GetMapping("/user")
    public BasUser getBasUserById(@RequestParam("user_id") String user_id){

        return basUserService.getById(user_id);

    }
    @ResponseBody
    @GetMapping("/findAll")
    public List<BasUser> getAll(){
        return basUserService.list();
    }
}
