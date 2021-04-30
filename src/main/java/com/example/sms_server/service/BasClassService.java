package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasClass;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.entity.display.DisplayClass;

import java.util.List;

public interface BasClassService extends IService<BasClass> {

    List<BasClass> getClassByTeacher(String teacherId);
    List<String> getClassIdByTeacher(String teacherId);
    List<String> getUnFullClassIds(Integer gradeId,String majorId);
    PageData<DisplayClass> findAllDisPlayClassPage(Long page,Long limit);
    PageData<DisplayClass> findAllDisplayClassPageByTeacherName(String teacherName,Long page,Long limit);
}
