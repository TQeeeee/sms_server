package com.example.sms_server.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResponseResult {
    // 50001是数据库方面的失败
    private int code = 20000;
    private String status = "success";
    private Object data;

    @Data
    @AllArgsConstructor
    public class Token {
        private String token;
    }
}
