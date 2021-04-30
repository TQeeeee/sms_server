package com.example.sms_server.mapper;

import com.example.sms_server.entity.BasClass;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BasClassMapper extends EasyBaseMapper<BasClass> {

    public List<BasClass> getClassByTeacher(String teacherId);
    public List<String> getClassIdByTeacher(String teacherId);
    public List<String> getUnFullClassIds(Integer gradeId, String majorId);
    public String getTeacherIdByClassId(String classId);

}
