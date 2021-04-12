package cn.com.authority.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResponseResult {
    private int code=20000;
    private String status="success";
    private Object data;

    @Data
    @AllArgsConstructor
    public class Token{
        private String token;
    }
}
