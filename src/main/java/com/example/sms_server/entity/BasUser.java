package com.example.sms_server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bas_user")
public class BasUser {
    //@TableField(exist = false)
    @TableId
    private String userId;
    private String userPassword;
    private String userName;
    @TableField("role_id")
    private Integer roleId;
    private Integer confirmed;

}
