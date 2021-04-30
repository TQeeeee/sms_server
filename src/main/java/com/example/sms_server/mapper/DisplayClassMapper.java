package com.example.sms_server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.entity.display.DisplayClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DisplayClassMapper {
    @Select("select * from display_class")
    Page<DisplayClass> getPageDisplayClass(Page<DisplayClass> iPage);

    @Select("select * from display_class where teacher_name like CONCAT('%',#{teacherName},'%')")
    Page<DisplayClass> getPageDisplayClassByTeacherName(Page<DisplayClass> iPage,String teacherName);

}
