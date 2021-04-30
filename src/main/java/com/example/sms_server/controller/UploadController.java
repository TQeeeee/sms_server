package com.example.sms_server.controller;

import com.example.sms_server.commons.ResponseResult;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/qiniu")
public class UploadController {


    private static final String AK = "5BbWkafRH6B0d_C9Ry2yWm7FS9_yCIMcCvonQpFD";
    private static final String SK = "eud3wYw-Rhz8iEe_xptOMc6cxqsVaFHmgkQElaV4";
    private static final String BUCKETNAME = "ytqgraduate";
    private static final String DOMIN = "qsbiu57b5.hn-bkt.clouddn.com";

    // 七牛文件上传管理器
    private UploadManager uploadManager;
    private String token;
    // 七牛认证管理
    private Auth auth;

    @GetMapping("/getToken")
    public ResponseResult getToken(){
        ResponseResult responseResult = new ResponseResult();
        auth = Auth.create(AK,SK);
        token = auth.uploadToken(BUCKETNAME);
        log.info("token is"+token);
        responseResult.setData(token);
        return responseResult;
    }
}
