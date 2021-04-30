package com.example.sms_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("temp_prize")
public class TempPrize {
    @TableField(value="student_id")
    private String studentId;
    private String prizeRecord;
    private Integer confirmed;
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    public  TempPrize(String studentId,String prizeRecord){
        this.studentId = studentId;
        this.prizeRecord = prizeRecord;
    }





}
