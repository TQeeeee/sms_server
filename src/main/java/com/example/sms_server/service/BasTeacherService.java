package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasTeacher;
import com.example.sms_server.entity.PageData;

public interface BasTeacherService extends IService<BasTeacher> {
    BasTeacher getById(String teacherId);
    void addTeacher(BasTeacher basTeacher);
    void deleteTeacher(String teacherId);
    public PageData<BasTeacher> findPage(BasTeacher basTeacher, Long page, Long limit);
    public PageData<BasTeacher> findPageByStudentName(String studentName,Long page,Long limit);
    String getEmailAddressById(String teacherId);
}
