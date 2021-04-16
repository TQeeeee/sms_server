package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @Override
    public Integer fastImportUser(List<BasUser> basUserList) {
        //将数据进行加密
        for(BasUser basUser :  basUserList){
            basUser.setUserPassword(EncryptUtil.getMD5Str(basUser.getUserPassword()));
        }
        //批量插入数据库中
        try {
            int cnt = basUserMapper.insertBatchSomeColumn(basUserList);
            return cnt;
        }catch (Exception e){
            return -1;
        }

    }

    @Override
    public void deleteBasUser(String userId) {
        basUserMapper.deleteById(userId);
    }

    @Override
    public IPage<BasUser> pageBasUser(IPage<BasUser> page, QueryWrapper<BasUser> basUserQueryWrapper) {
        return basUserMapper.selectPage(page,basUserQueryWrapper);
    }
}
