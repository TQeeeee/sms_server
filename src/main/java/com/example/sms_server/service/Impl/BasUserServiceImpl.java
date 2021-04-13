package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.mapper.BasUserMapper;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasUserServiceImpl extends ServiceImpl<BasUserMapper,BasUser> implements BasUserService {

    @Autowired
    BasUserMapper basUserMapper;

    public BasUser getById(String id){
        return basUserMapper.getById(id);
    }

    public BasUser findByUserName(String username) {
        return basUserMapper.findByUserName(username);
    }

    @Override
    public void addUser(BasUser basUser) {
        basUser.setUserPassword(EncryptUtil.getMD5Str(basUser.getUserPassword()));
        basUserMapper.insert(basUser);
    }
}
