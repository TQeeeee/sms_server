package com.example.sms_server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.service.BasUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmsServerApplicationTests {

    @Autowired
    BasUserService basUserService;
    @Test
    void contextLoads() {
    }
    @Test
    public void TestPage(){
        Page<BasUser> page = new Page<>(2,1);
        basUserService.page(page);
        page.getRecords().forEach(System.out::println);
    }
}
