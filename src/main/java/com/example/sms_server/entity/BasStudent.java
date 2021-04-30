package com.example.sms_server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bas_student")
public class BasStudent implements Serializable {
    @TableId
    private String studentId;
    private String actualName;
    private String telephone;
    private String emailAddress;
    private String address;
    private String gender;
    //@TableField(value = "age" , condition = "%s&gt;#{%s}")
    private Integer age;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date start;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date end;
    private String classId;//不是让学生填的
    //@TableField(value = "gradeId" , condition = "%s&gt;#{%s}")
    private Integer gradeId;//不是学生填的
    private String majorId; //是让学生填的，用下拉框进行选择
    private Integer finished;  //这个是管理员填的
    private Integer confirmed;  //这个在数据库表中有记录后要返回给学生
    private String prizeRecord; //在管理员审核后将其填入该表的这一字段
}
