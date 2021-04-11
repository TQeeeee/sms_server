package com.example.sms_server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value="bas_user")
public class BasUser {
    //@TableField(exist = false)
    @TableId
    private String userId;
    private String userPassword;
    private String userName;
    @TableField("role_id")
    private Long roleId;
    private Long userSalt;
}
