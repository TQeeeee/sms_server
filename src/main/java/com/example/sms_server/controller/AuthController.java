package com.example.sms_server.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.sms_server.commons.ResponseResult;
import com.example.sms_server.entity.AuthSysAccount;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.service.resetPasswordService;
import com.example.sms_server.utils.EncryptUtil;
import com.example.sms_server.utils.JwtUtil;
import com.example.sms_server.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//拦截用户登录请求和鉴权
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BasUserService basUserService;
    @Autowired
    private resetPasswordService resetPasswordService;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody AuthSysAccount account, HttpSession session) {
//    @RequestMapping("/login")
//    public ResponseResult login(AuthSysAccount account, HttpSession session){
        //System.out.println(session.getId());
        ResponseResult result = new ResponseResult();
        // 1 从Shiro框架中，获取一个Subject对象，代表当前会话的用户
        Subject subject = SecurityUtils.getSubject();
        // 2 认证subject的身份
        // 2.1 封装要认证的身份信息
        AuthenticationToken token = new UsernamePasswordToken(account.getUserid(), EncryptUtil.getMD5Str(account.getPassword()));
        // 2.2 认证：认证失败，抛异常
        try {
            subject.login(token);
            String resToken = JwtUtil.sign(account.getUserid());
            result.setData(result.new Token(resToken));
            // 在session中保存登录标记
//            session.setAttribute(resToken, account.getUsername());
            this.redisUtil.set(resToken, account.getUserid(), 30 * 60);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData("用户名或密码错误");
        }
        return result;
    }

    @GetMapping("/login")
    public void loginPage(HttpServletResponse res) {
        try {
            res.sendRedirect("http://localhost:9527");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/noLogin")
    public ResponseResult noLogin(){
        ResponseResult result = new ResponseResult();
        result.setCode(20001);
        result.setData("没有登录，请登录先");
        return result;
    }

    @GetMapping("/info")
    public ResponseResult info(@RequestHeader("X-Token") String token, HttpSession session) {
        //System.out.println(session.getId());
        ResponseResult result = new ResponseResult();

        String userid = JwtUtil.verityToken(token, "userId");
        if (userid == null) {
            throw new RuntimeException("没有登录");
        } else {
            BasUser basUser = this.basUserService.getById(userid);
            if (basUser == null) {
                throw new RuntimeException("没有登录");
            } else {
                result.setData(basUser);
            }
        }
        return result;
    }
    //用户未登录，忘记密码------咋办？但是该用户确实是系统用户
    @PostMapping("/forgetPassword")
    public ResponseResult forgetPassword(@RequestBody JSONObject jsonObject){
        ResponseResult responseResult = new ResponseResult();
        log.info(jsonObject.getString("userId"));
        try {
            resetPasswordService.resetUserPassword(jsonObject.getString("userId"));
        } catch (RuntimeException e){
            responseResult.setCode(50001);
            responseResult.setData(e.getMessage());
        }
        return responseResult;
    }



    @PostMapping("/logout")
    public ResponseResult logOut(@RequestHeader("X-Token") String token){
        ResponseResult result = new ResponseResult();
        //session.removeAttribute(token);
        //session.invalidate();
        SecurityUtils.getSubject().logout();
        // 删除redis中的标记
        this.redisUtil.del(token);
        return result;
    }

}
