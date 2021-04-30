package com.example.sms_server.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.*;
import com.example.sms_server.mapper.*;
import com.example.sms_server.service.BasStudentService;
import com.example.sms_server.service.BasTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
@Slf4j
@Service
public class BasStudentServiceImpl extends ServiceImpl<BasStudentMapper, BasStudent> implements BasStudentService {

    @Autowired
    BasUserMapper basUserMapper;
    @Autowired
    BasClassMapper basClassMapper;
    @Autowired
    BasStudentMapper basStudentMapper;
    @Autowired
    BasOldStudentMapper basOldStudentMapper;
    private BasStudent basStudent;

    //student表增加记录，user字段置1，class表增加实际人数
    @Override
    @Transactional
    public void addStudent(BasStudent basStudent) {
        this.basStudent = basStudent;
        setExtraStudentInfo();
        basStudentMapper.insert(basStudent);
        //更新班级人数
        BasClass basClass = basClassMapper.selectById(basStudent.getClassId());
        basClass.setStudentCount(basClass.getStudentCount()+1);
        basClassMapper.updateById(basClass);
        //更新
        //用户表中该用户已经完成信息注册
        BasUser basUser = basUserMapper.selectById(basStudent.getStudentId());
        basUser.setConfirmed(Integer.parseInt("1"));
        basUserMapper.updateById(basUser);
    }

    //1、该学生是哪个年级2、查询该专业该年级有哪些班没有满3、给学生设置班级，及入学时间
    private void setExtraStudentInfo(){
        //学生年级
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date(System.currentTimeMillis());
        Integer gradeId = Integer.parseInt(sdf.format(date));
        //Integer gradeId = Calendar.YEAR;
        String majorId = basStudent.getMajorId();
        //查看哪些班没有满
        List<String> unFullClassLists = basClassMapper.getUnFullClassIds(gradeId, majorId);
        String classId = unFullClassLists.get(0);
        //设置入学年份，班级，年级
        basStudent.setStart(new Date(System.currentTimeMillis()));
        basStudent.setGradeId(gradeId);
        basStudent.setClassId(classId);

    }

    //教师查询 或管理员查询
    @Override
    public PageData<BasStudent> findPage(BasStudent basStudent, String teacherId, Long page, Long limit) {
        QueryWrapper<BasStudent> queryWrapper = new QueryWrapper<>(basStudent);
        if(teacherId != null){
            //根据班级ID和学生本身的查询条件获取到所有学生信息
            List<String> classIds = basClassMapper.getClassIdByTeacher(teacherId);
            queryWrapper.in("class_id", classIds);
        }
        Page<BasStudent> page1 = new Page<>(page,limit);
        IPage<BasStudent> iPage;
        iPage = basStudentMapper.selectPage(page1,queryWrapper);
        //封装到PageData<BasStudent>中返回
        PageData<BasStudent> pageData = new PageData<>(iPage.getTotal(),iPage.getRecords());
        return pageData;
    }


    //其实是将删除的学生迁移到了旧学生表中
    //在用户表中将其删除
    @Override
    @Transactional
    public void deleteStudent(String studentId) {
        BasOldStudent basOldStudent = generateOldStudent(studentId);
        //插入旧表
        basOldStudent.setEnd(new Date(System.currentTimeMillis()));
        basOldStudentMapper.insert(basOldStudent);
        //删除学生表和用户表中的数据
        basStudentMapper.deleteById(studentId);
        basUserMapper.deleteById(studentId);
    }

    private BasOldStudent generateOldStudent(String studentId){
        BasStudent basStudent = basStudentMapper.selectById(studentId);
        Student2OldMapper s2oMapper = Mappers.getMapper(Student2OldMapper.class);
        BasOldStudent basOldStudent = s2oMapper.student2Old(basStudent);
        return basOldStudent;
    }


    @Override
    public String getEmailAddressById(String studentId) {
        return basStudentMapper.getEmailAddressById(studentId);
    }
}
