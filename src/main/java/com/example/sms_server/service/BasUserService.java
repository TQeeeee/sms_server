package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.mapper.BasUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface BasUserService extends IService<BasUser> {


    BasUser getById(String id);
}
