package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.BasOldStudent;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.mapper.BasOldStudentMapper;
import com.example.sms_server.service.BasOldStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasOldStudentServiceImpl extends ServiceImpl<BasOldStudentMapper, BasOldStudent>
    implements BasOldStudentService {

    @Autowired
    BasOldStudentMapper basOldStudentMapper;

    @Override
    public PageData<BasOldStudent> findPage(BasOldStudent basOldStudent, Long page, Long limit) {
        QueryWrapper<BasOldStudent> basOldStudentQueryWrapper = new QueryWrapper<>(basOldStudent);
        Page<BasOldStudent> page1 = new Page<>(page,limit);
        IPage<BasOldStudent> iPage;
        iPage = basOldStudentMapper.selectPage(page1,basOldStudentQueryWrapper);
        PageData<BasOldStudent> pageData = new PageData<BasOldStudent>(iPage.getTotal(),iPage.getRecords());


        return pageData;
    }
}
