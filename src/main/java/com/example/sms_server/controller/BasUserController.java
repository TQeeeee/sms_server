package com.example.sms_server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.BasStudent;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.entity.PageData;
import com.example.sms_server.service.BasStudentService;
import com.example.sms_server.service.BasTeacherService;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.utils.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class BasUserController {
    @Autowired
    BasUserService basUserService;
    @Autowired
    BasTeacherService basTeacherService;
    @Autowired
    BasStudentService basStudentService;

    @ResponseBody
    @GetMapping("/get")
    public BasUser getBasUserById(@RequestParam("user_id") String user_id){

        return basUserService.getById(user_id);

    }
    //添加用户-----单条记录并且加密
    @PostMapping("/create")
    public ResponseResult addBasUser(@RequestBody BasUser basUser) {
        ResponseResult responseResult = new ResponseResult();
        basUserService.addUser(basUser);
        return responseResult;

    }

    //添加用户 -----批量插入并且加密
    @PostMapping("/import")
    public ResponseResult importBasUsers(@RequestBody String importData) {
        ResponseResult responseResult = new ResponseResult();
        List<BasUser> basUsers = JSON.parseArray(importData,BasUser.class);

        int cnt = basUserService.fastImportUser(basUsers);

        if(cnt == -1){
            responseResult.setCode(20005);
            responseResult.setData("导入失败");
        }

        return responseResult;
    }
    //后端分页：按照用户传入的page和limit，还有用户名返回对应页的数据
    //返回数据格式："data":{"total":100,"items":[{"userId":"800tq","userName":"xxxx"},{}]}
    @GetMapping("/findPage")
    public ResponseResult findPage(HttpServletRequest request) {
        QueryWrapper<BasUser> basUserQueryWrapper = new QueryWrapper<>();
        //如果查询条件中有UserId
        if (request.getParameter("userName") != null) {
            basUserQueryWrapper.like("user_name", request.getParameter("userName"));
        }
        Page<BasUser> page = new Page<>(Long.parseLong(request.getParameter("page"))
                , Long.parseLong(request.getParameter("limit")));
        // 作者建议：wrpper的使用成本很高，使用需慎重（待改进）
        IPage<BasUser> iPage = basUserService.pageBasUser(page, basUserQueryWrapper);
        //获取符合标准的记录总数
        PageData<BasUser> data = new PageData<>(iPage.getTotal(), iPage.getRecords());
        ResponseResult responseResult = new ResponseResult();
        responseResult.setData(data);
        //接下来需将查询数据转化为json字符串传给前端
        return responseResult;
    }
    //

    @PutMapping("/update")
    public ResponseResult update(@RequestBody BasUser basUser){
        basUser.setUserPassword(EncryptUtil.getMD5Str(basUser.getUserPassword()));
        basUserService.updateById(basUser);
        return new ResponseResult();
    }

    //用户自己本身更改密码(已经登录成功后)-----前端参数put方式{“tempUserId”:,,"tempPassWord"}
    @PutMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody String tempUser){
        JSONObject jsonObject = JSONObject.parseObject(tempUser);
        String userId = jsonObject.getString("userId");
        String newPassword = jsonObject.getString("newPassword");
        BasUser basUser = basUserService.getById(userId);
        basUser.setUserPassword(EncryptUtil.getMD5Str(newPassword));
        basUserService.updateById(basUser);
        return new ResponseResult();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String userId){
        ResponseResult responseResult = new ResponseResult();
        // 不能简单的说删除就删除，如果没有确认的可以删除，如果已经确认了的，就要派发到对应的service中去删除
        BasUser basUser = basUserService.getById(userId);
        // 如果还未确认或角色是管理员，直接删除
        if(basUser.getConfirmed() == 0 || 1 == basUser.getRoleId())
            basUserService.deleteBasUser(userId);
        else{
            //如果是老师，就派发到删除老师那里
            if(basUser.getRoleId() == 2){
                try{
                    basTeacherService.deleteTeacher(basUser.getUserId());
                } catch (Exception e){
                    responseResult.setData(e.getMessage());
                }
            }else{
                try{
                    basStudentService.deleteStudent(basUser.getUserId());
                } catch (Exception e){
                    log.info("学生删除异常"+e.getMessage());
                    responseResult.setData("学生删除失败");
                }

            }
        }
        return responseResult;
    }



}
