package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasOldStudent;
import com.example.sms_server.entity.PageData;

public interface BasOldStudentService extends IService<BasOldStudent> {
    public PageData<BasOldStudent> findPage(BasOldStudent basOldStudent, Long page, Long limit);
}
