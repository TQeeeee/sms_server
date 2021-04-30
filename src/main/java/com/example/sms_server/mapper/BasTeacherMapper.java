package com.example.sms_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sms_server.entity.BasMajor;
import com.example.sms_server.entity.BasTeacher;
import com.example.sms_server.entity.BasUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BasTeacherMapper extends EasyBaseMapper<BasTeacher> {
    BasTeacher getById(String teacherId);
    public String getEmailAddressById(String teacherId);
}
