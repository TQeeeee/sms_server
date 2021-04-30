package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.*;
import com.example.sms_server.mapper.BasClassMapper;
import com.example.sms_server.mapper.BasStudentMapper;
import com.example.sms_server.mapper.BasTeacherMapper;
import com.example.sms_server.mapper.BasUserMapper;
import com.example.sms_server.service.BasTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BasTeacherServiceImpl extends ServiceImpl<BasTeacherMapper, BasTeacher> implements BasTeacherService {

    @Autowired
    BasTeacherMapper basTeacherMapper;
    @Autowired
    BasUserMapper basUserMapper;
    @Autowired
    BasClassMapper basClassMapper;
    @Autowired
    BasStudentMapper basStudentMapper;
    private BasTeacher basTeacher;
    private Integer otherTeacherCount;
    private List<BasTeacher> otherTeacherList;


    @Override
    public BasTeacher getById(String teacherId) {
        return basTeacherMapper.getById(teacherId);
    }

    //添加教师需要1、添加记录到教师表
    //2、更改用户表中的该对应字段
    @Override
    @Transactional
    public void addTeacher(BasTeacher basTeacher) {
        basTeacherMapper.insert(basTeacher);
        BasUser basUser = basUserMapper.getById(basTeacher.getTeacherId());
        basUser.setConfirmed(Integer.parseInt("1"));
        basUserMapper.updateById(basUser);

    }

    //删除教师，需要1、将该教师所带班级重新分配教师（系统先随机指定，到时候管理员再改）
    // 2、删除教师表中的记录
    //3、删除用户表中的记录
    @Override
    @Transactional
    public void deleteTeacher(String teacherId) {
        //先查询所有的班级
        if (!canDelete(teacherId)) throw new RuntimeException("教师人数过少，不可删除！");
        else {
            List<BasClass> basClassList = basClassMapper.getClassByTeacher(teacherId);
            Integer sadClassCount = basClassList.size();
            BasClass basClass;
            for (int index = 0; index < sadClassCount; index++) {
                basClass = basClassList.get(index);
                //获取当前班级的索引，如13号班级，5个老师，对应13%5 = 3
                basClass.setTeacherId(otherTeacherList
                        .get(index % otherTeacherCount).getTeacherId());
                basClassMapper.updateById(basClass);
            }
            basTeacherMapper.deleteById(teacherId);
            basUserMapper.deleteById(teacherId);
        }
    }

    private Boolean canDelete(String teacherId) {
        QueryWrapper<BasTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.ne("teacher_id", teacherId);
        this.otherTeacherList = basTeacherMapper.selectList(teacherQueryWrapper);
        this.otherTeacherCount = otherTeacherList.size();
        if (otherTeacherCount == 0) return false;
        else return true;
    }

    @Override
    public PageData<BasTeacher> findPage(BasTeacher basTeacher, Long page, Long limit) {
        QueryWrapper<BasTeacher> queryWrapper = new QueryWrapper<>(basTeacher);
        Page<BasTeacher> page2 = new Page<>(page, limit);
        IPage<BasTeacher> iPage;
        iPage = basTeacherMapper.selectPage(page2, queryWrapper);
        //封装到PageData<BasStudent>中返回
        PageData<BasTeacher> pageData = new PageData<>(iPage.getTotal(), iPage.getRecords());
        return pageData;

    }

    @Override
    public PageData<BasTeacher> findPageByStudentName(String studentName, Long page, Long limit) {
        //先根据学生姓名查出班级
        //根据班级查询到教师id
        List<String> teacherIds = getTeacherIds(studentName);
        //根据教师id查询出完整信息
        QueryWrapper<BasTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.in("teacher_id",teacherIds);
        Page<BasTeacher> page1 = new Page<>(page,limit);
        IPage<BasTeacher> iPage;
        iPage = basTeacherMapper.selectPage(page1,teacherQueryWrapper);
        //封装到PageData<BasStudent>中返回
        PageData<BasTeacher> pageData = new PageData<>(iPage.getTotal(),iPage.getRecords());
        return pageData;
    }
    private List<String> getTeacherIds(String studentName){
        QueryWrapper<BasStudent> studentQueryWrapper = new QueryWrapper<BasStudent>();
        studentQueryWrapper.eq("actual_name",studentName);
        List<BasStudent> studentList = basStudentMapper.selectList(studentQueryWrapper);
        List<String> classIds = new ArrayList<>();
        List<String> teacherIds = new ArrayList<>();
        for(BasStudent basStudent : studentList){
            classIds.add(basStudent.getClassId());
        }
        // 获取到了教师id集合
        for(String classId : classIds){
            teacherIds.add(basClassMapper.getTeacherIdByClassId(classId));
        }
        return teacherIds;
    }

    @Override
    public String getEmailAddressById(String teacherId) {

        return null;
    }


}
