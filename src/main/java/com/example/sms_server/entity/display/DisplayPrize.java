package com.example.sms_server.entity.display;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayPrize {
    private String studentId;
    private String studentName;
    private Integer gradeId;
    private String classId;
    private String className;
    private String teacherId;
    private String teacherName;
    private String prizeRecord;
    private Integer confirmed;
    private Integer recordId;
}
