package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasStudent;
import com.example.sms_server.entity.PageData;

public interface BasStudentService extends IService<BasStudent> {
    public void addStudent(BasStudent basStudent);
    public void deleteStudent(String studentId);
    //教师查询分页
    public PageData<BasStudent> findPage(BasStudent basStudent,String teacherId,Long page,Long limit);
    String getEmailAddressById(String studentId);
}
