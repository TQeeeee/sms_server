package com.example.sms_server.entity.display;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 类似于一个专业的视图
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayMajor {
    String majorId;
    String majorName;
    Integer classNumber;
    Integer studentNumber;
}
