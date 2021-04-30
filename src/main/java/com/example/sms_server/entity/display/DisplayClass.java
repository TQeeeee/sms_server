package com.example.sms_server.entity.display;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//这是构造的一个视图


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayClass {
    private String classId;
    private Integer gradeId;
    private String majorName;
    private String teacherName;
    private String className;
    private Integer studentCount;
    private Integer maxCount;
}
