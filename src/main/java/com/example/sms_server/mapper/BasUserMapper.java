package com.example.sms_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sms_server.entity.BasUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface BasUserMapper extends BaseMapper<BasUser> {
    //继承了BaseMapper之后，基本的crud功能不必再专门定义方法
    //@Select("select * from bas_user where user_id=#{id}")
    //BasUser getById(Integer id);
    BasUser getById(String id);

}
