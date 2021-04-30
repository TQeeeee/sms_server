package com.example.sms_server.entity;

import lombok.Data;

@Data
public class MsgEntity {


    private String userId;

    private String username;

    private String msg;

    private int count;

}