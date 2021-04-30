package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.BasClass;
import com.example.sms_server.entity.BasMajor;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.entity.display.DisplayMajor;
import com.example.sms_server.mapper.BasClassMapper;
import com.example.sms_server.mapper.BasMajorMapper;
import com.example.sms_server.service.BasMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasMajorServiceImpl extends ServiceImpl<BasMajorMapper, BasMajor> implements BasMajorService {
    @Autowired
    BasMajorMapper basMajorMapper;
    @Autowired
    BasClassMapper basClassMapper;
    private Integer classNumber;
    private Integer studentNumber;

    @Override
    public List<DisplayMajor> findAllDisplayMajor() {
        //不再写xml语句，直接在这个之中赋值
        // 先查出所有的专业
        List<DisplayMajor> displayMajors = new ArrayList<>();
        QueryWrapper<BasMajor> basMajorQueryWrapper = new QueryWrapper<>();
        List<BasMajor> basMajors = basMajorMapper.selectList(basMajorQueryWrapper);
        for(BasMajor basMajor : basMajors){
            DisplayMajor displayMajor = new DisplayMajor();
            displayMajor.setMajorId(basMajor.getMajorId());
            displayMajor.setMajorName(basMajor.getMajorName());
            displayMajor.setClassNumber(countClassNumber(basMajor.getMajorId()));
            displayMajor.setStudentNumber(countStudentNumber(basMajor.getMajorId()));
            displayMajors.add(displayMajor);
        }

        return displayMajors;
        //查出所有的班级
        //查出班级所有的实际人数
    }
    private Integer countClassNumber(String majorId){
        QueryWrapper<BasClass> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("major_id",majorId);
        List<BasClass> basClassList = basClassMapper.selectList(classQueryWrapper);
        return basClassList.size();

    }
    private Integer countStudentNumber(String majorId){
        Integer studentNumber = 0;
        QueryWrapper<BasClass> classQueryWrapper = new QueryWrapper<>();
        classQueryWrapper.eq("major_id",majorId);
        List<BasClass> basClassList = basClassMapper.selectList(classQueryWrapper);
        for(BasClass basClass : basClassList){
            studentNumber += basClass.getStudentCount();
        }
        return studentNumber;

    }



}
