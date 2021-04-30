package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sms_server.entity.BasStudent;
import com.example.sms_server.entity.BasTeacher;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.mapper.BasUserMapper;
import com.example.sms_server.service.BasStudentService;
import com.example.sms_server.service.BasTeacherService;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.service.resetPasswordService;
import com.example.sms_server.utils.EncryptUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class resetPasswordServiceImpl implements resetPasswordService {
    @Autowired
    BasUserService basUserService;
    @Autowired
    BasTeacherService basTeacherService;
    @Autowired
    BasStudentService basStudentService;
    @Autowired
    JavaMailSenderImpl javaMailSender;


    @Value("${spring.mail.username}")
    private String from;
    private String tempPassword;
    private String actualName;
    private String emailAddress;
    private Integer roleId;
    private String userId;
    private BasUser basUser;

    @Override
    public int resetUserPassword(String userId) {
        this.basUser = basUserService.getById(userId);
        //只要确认了，表中就肯定有记录
        if (basUser == null || basUser.getConfirmed() == 0) {
            throw new RuntimeException("系统中无此用户或该用户未确认");
        } else {
            this.roleId = basUser.getRoleId();
            this.userId = userId;
        }
        //更新密码应该不会失败。。。。。
        try {
            getEmailAddressUpdatePassWord();

        } catch (RuntimeException e){
            throw new RuntimeException("无法获取用户信息");
        }
        try{
            SendMail();
        } catch (RuntimeException e){
            throw new RuntimeException("无法发送邮件");
        }
        return 0;
    }

    //该方法用来获取邮件地址和更新数据库记录
    private void getEmailAddressUpdatePassWord() {
        if (2 == this.roleId) {
            QueryWrapper<BasTeacher> teacherQueryWrapper = new QueryWrapper<>();
            teacherQueryWrapper.eq("teacher_id", userId);
            //获取到用户，将email地址和真实姓名拿出来，并且更新
            BasTeacher basTeacher = basTeacherService.getOne(teacherQueryWrapper);
            this.actualName = basTeacher.getActualName();
            this.emailAddress = basTeacher.getEmailAddress();
        } else if (3 == this.roleId) {
            QueryWrapper<BasStudent> studentQueryWrapper = new QueryWrapper<>();
            studentQueryWrapper.eq("student_id", userId);
            //获取到用户，将email地址和真实姓名拿出来，并且更新
            BasStudent basStudent = basStudentService.getOne(studentQueryWrapper);
            this.actualName = basStudent.getActualName();
            this.emailAddress = basStudent.getEmailAddress();
        }
        //构造一个6位的临时密码，把它更新到用户的数据库中
        this.tempPassword = RandomStringUtils.random(6, true, true);
        this.basUser.setUserPassword(EncryptUtil.getMD5Str(this.tempPassword));
        basUserService.updateById(this.basUser);
    }


    //该方法用来发送邮件
    private void SendMail() {
        //创建一个简单邮件
        SimpleMailMessage message = new SimpleMailMessage();
        //2.设置邮件主题
        message.setSubject("主题：这是一封密码找回的测试邮件");
        //3.设置邮件发送者
        message.setFrom(from);
        //4.设置邮件接收者，可以有多个接收者
        //message.setTo("798327052@qq.com");
        message.setTo(this.emailAddress);
        //8.设置邮件的正文
        message.setText(this.actualName+"\n"+"学籍管理系统：你的临时密码为"+this.tempPassword+"\n"+"请尽快登录后更改");
        //9.发送邮件
        javaMailSender.send(message);
    }
}
