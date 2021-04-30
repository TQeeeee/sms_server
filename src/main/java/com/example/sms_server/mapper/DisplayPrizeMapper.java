package com.example.sms_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.entity.display.DisplayPrize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DisplayPrizeMapper extends BaseMapper<DisplayPrize> {
    //根据教师id查找分页数据

    @Select("select * from display_prize")
    Page<DisplayPrize> getPageDisplayPrize(Page<DisplayPrize> iPage);

    @Select("select * from display_class where teacher_name like CONCAT('%',#{teacherName},'%')")
    Page<DisplayPrize> getPageDisplayPrizeByTeacherName(Page<DisplayPrize> iPage,String teacherName);




    //根据教师姓名查找分页数据
    //直接查找所有分页数据


}
