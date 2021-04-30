package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.BasClass;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.entity.display.DisplayClass;
import com.example.sms_server.mapper.BasClassMapper;
import com.example.sms_server.mapper.DisplayClassMapper;
import com.example.sms_server.service.BasClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BasClassServiceImpl extends ServiceImpl<BasClassMapper, BasClass> implements BasClassService {
    @Autowired
    BasClassMapper basClassMapper;
    @Autowired
    DisplayClassMapper displayClassMapper;
    @Override
    public List<BasClass> getClassByTeacher(String teacherId) {
        return basClassMapper.getClassByTeacher(teacherId);
    }
    @Override
    public List<String> getClassIdByTeacher(String teacherId){
        return basClassMapper.getClassIdByTeacher(teacherId);
    }

    @Override
    public List<String> getUnFullClassIds(Integer gradeId, String majorId) {
        return basClassMapper.getUnFullClassIds(gradeId,majorId);
    }

    @Override
    public PageData<DisplayClass> findAllDisPlayClassPage(Long page, Long limit) {
        Page<DisplayClass> page1 = new Page<>(page,limit);
        Page<DisplayClass> pageDisplayClass = displayClassMapper.getPageDisplayClass(page1);
        PageData<DisplayClass> pageData = new PageData<DisplayClass>(pageDisplayClass.getTotal(),pageDisplayClass.getRecords());
        return pageData;
    }

    @Override
    public PageData<DisplayClass> findAllDisplayClassPageByTeacherName(String teacherName
            , Long page, Long limit) {
        Page<DisplayClass> page2 = new Page<>(page,limit);
        Page<DisplayClass> displayClassPage = displayClassMapper.getPageDisplayClassByTeacherName(
                page2,teacherName
        );
        PageData<DisplayClass> pageData = new PageData<DisplayClass>(displayClassPage.getTotal(),displayClassPage.getRecords());
        return pageData;
    }
}
