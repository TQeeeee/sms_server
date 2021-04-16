package com.example.sms_server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.utils.EncryptUtil;
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
    @Test
    public void TestUpdate(){

        //String data = "{"userList":[{"userIndex":1,"userId":11110,"userPassword":123456,"userName":"tq01","roleId":1},{"userIndex":2,"userId":11111,"userPassword":123456,"userName":"tq02","roleId":2},{"userIndex":3,"userId":11112,"userPassword":123456,"userName":"tq03","roleId":3},{"userIndex":4,"userId":11113,"userPassword":123456,"userName":"tq04","roleId":1},{"userIndex":5,"userId":11114,"userPassword":123456,"userName":"tq05","roleId":1},{"userIndex":6,"userId":11115,"userPassword":123456,"userName":"tq06","roleId":1},{"userIndex":7,"userId":11116,"userPassword":123456,"userName":"tq07","roleId":1},{"userIndex":8,"userId":11117,"userPassword":123456,"userName":"tq08","roleId":1},{"userIndex":9,"userId":11118,"userPassword":123456,"userName":"tq09","roleId":1},{"userIndex":10,"userId":11119,"userPassword":123456,"userName":"tq10","roleId":1},{"userIndex":11,"userId":11120,"userPassword":123456,"userName":"tq11","roleId":1},{"userIndex":12,"userId":11121,"userPassword":123456,"userName":"tq12","roleId":1},{"userIndex":13,"userId":11122,"userPassword":123456,"userName":"tq13","roleId":1},{"userIndex":14,"userId":11123,"userPassword":123456,"userName":"tq14","roleId":1},{"userIndex":15,"userId":11124,"userPassword":123456,"userName":"tq15","roleId":1},{"userIndex":16,"userId":11125,"userPassword":123456,"userName":"tq16","roleId":1},{"userIndex":17,"userId":11126,"userPassword":123456,"userName":"tq17","roleId":1},{"userIndex":18,"userId":11127,"userPassword":123456,"userName":"tq18","roleId":1},{"userIndex":19,"userId":11128,"userPassword":123456,"userName":"tq19","roleId":1},{"userIndex":20,"userId":11129,"userPassword":123456,"userName":"tq20","roleId":1},{"userIndex":21,"userId":11130,"userPassword":123456,"userName":"tq21","roleId":1},{"userIndex":22,"userId":11131,"userPassword":123456,"userName":"tq22","roleId":1},{"userIndex":23,"userId":11132,"userPassword":123456,"userName":"tq23","roleId":1},{"userIndex":24,"userId":11133,"userPassword":123456,"userName":"tq24","roleId":1},{"userIndex":25,"userId":11134,"userPassword":123456,"userName":"tq25","roleId":1},{"userIndex":26,"userId":11135,"userPassword":123456,"userName":"tq26","roleId":1},{"userIndex":27,"userId":11136,"userPassword":123456,"userName":"tq27","roleId":1},{"userIndex":28,"userId":11137,"userPassword":123456,"userName":"tq28","roleId":1},{"userIndex":29,"userId":11138,"userPassword":123456,"userName":"tq29","roleId":1}]}";
        BasUser basUser = new BasUser();
        basUser.setUserId("11111");
        basUser.setUserName("gengxin");
        basUser.setUserPassword("123456");
        basUser.setRoleId(new Long(1));
        basUser.setUserPassword(EncryptUtil.getMD5Str(basUser.getUserPassword()));
        basUserService.updateById(basUser);
    }
}
