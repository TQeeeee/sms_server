package com.example.sms_server.mapper;

import com.example.sms_server.entity.BasOldStudent;
import com.example.sms_server.entity.BasStudent;
import org.mapstruct.Mapper;

@Mapper
public interface Student2OldMapper {
    BasOldStudent student2Old(BasStudent basStudent);
}
