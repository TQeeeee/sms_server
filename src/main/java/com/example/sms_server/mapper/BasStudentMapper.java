package com.example.sms_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sms_server.entity.BasStudent;
import com.example.sms_server.entity.BasUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BasStudentMapper extends EasyBaseMapper<BasStudent> {
    public String getEmailAddressById(String studentId);
}
