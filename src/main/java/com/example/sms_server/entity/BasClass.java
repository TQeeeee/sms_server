package com.example.sms_server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bas_class")
public class BasClass {
    @TableId
    private String classId;
    private Integer gradeId;
    private String majorId;
    private String teacherId;
    private String className;
    private Integer studentCount;
    private Integer maxCount;

}
