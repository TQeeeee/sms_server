package com.example.sms_server;

import com.example.sms_server.service.*;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SmsServerApplicationTests {


    @Autowired
    BasClassService basClassService;
    @Autowired
    DisplayPrizeService displayPrizeService;

    private static final String AK = "5BbWkafRH6B0d_C9Ry2yWm7FS9_yCIMcCvonQpFD";
    private static final String SK = "eud3wYw-Rhz8iEe_xptOMc6cxqsVaFHmgkQElaV4";
    private static final String BUCKETNAME = "ytqgraduate";
    private static final String DOMIN = "qsbiu57b5.hn-bkt.clouddn.com";


    // 七牛文件上传管理器
    private UploadManager uploadManager;
    private String token;
    // 七牛认证管理
    private Auth auth;



    @Test
    void contextLoads() {
    }

   @Test
    public void TestGetToken(){
       //CloudStorageConfig config = ApplicationContextUtil.getApplicationContext().getBean(CloudStorageConfig.class);
       //config.getQiniuBucketName();
       //uploadManager = new UploadManager(new Configuration(Zone.zone2()));
       auth = Auth.create(AK,SK);
       token = auth.uploadToken(BUCKETNAME);
       log.info("token is"+token);
   }





}
