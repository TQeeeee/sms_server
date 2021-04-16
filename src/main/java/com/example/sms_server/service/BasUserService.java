package com.example.sms_server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.mapper.BasUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface BasUserService extends IService<BasUser> {


    BasUser getById(String id);

    //根据用户名获取用户
    //获取所有用户 ------可以写出也可以不写，可以直接调用该服务的这一方法
    BasUser findByUserName(String username);

    //将加密后的用户数据插入数据库
    void addUser(BasUser basUser);
    //批量插入数据库---批量的好处：可以避免程序和数据库建立多次连接，增加服务器负荷。

    Integer fastImportUser(List<BasUser> basUserList);

    //用户确认

    //后端分页
    public IPage<BasUser> pageBasUser(IPage<BasUser> page, QueryWrapper<BasUser> basUserQueryWrapper);


    //根据用户ID删除用户
    void deleteBasUser(String userId);

}
