package com.example.sms_server;

import com.example.sms_server.utils.ApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SmsServerApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext =
        SpringApplication.run(SmsServerApplication.class, args);

        ApplicationContextUtil.setApplicationContext(applicationContext);
    }

}
