package com.example.sms_server.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.sms_server.entity.BasUser;
import com.example.sms_server.entity.MsgEntity;
import com.example.sms_server.mapper.BasUserMapper;
import com.example.sms_server.service.BasUserService;
import com.example.sms_server.service.Impl.BasUserServiceImpl;
import com.example.sms_server.utils.ApplicationContextUtil;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


// sid是房间号，userId是用户账号
@Slf4j
@Component
@ServerEndpoint("/groupChat/{sid}/{userId}")
public class WebSocketServerController {



    /**
     * 房间号 -> 组成员信息
     */
//    private static ConcurrentHashMap<String, List<Session>> groupMemberInfoMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, List<Session>> groupMemberInfoMap = new ConcurrentHashMap<>();
    /**
     * 房间号 -> 在线人数
     */
    private static ConcurrentHashMap<String, Set<String>> onlineUserMap = new ConcurrentHashMap<>();

    /**
     * 收到消息调用的方法，群成员发送消息
     *
     * @param sid:房间号
     * @param userId：用户id
     * @param message：发送消息
     */
    @OnMessage
    public void onMessage(@PathParam("sid") String sid, @PathParam("userId") String userId, String message) {
        List<Session> sessionList = groupMemberInfoMap.get(sid);
        Set<String> onlineUserList = onlineUserMap.get(sid);
        // 先一个群组内的成员发送消息
        sessionList.forEach(item -> {
            try {
                // json字符串转对象
                MsgEntity msg = JSONObject.parseObject(message, MsgEntity.class);
                msg.setCount(onlineUserList.size());
                // json对象转字符串
                String text = JSONObject.toJSONString(msg);
                item.getBasicRemote().sendText(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 建立连接调用的方法，群成员加入
     *
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("userId") String userId) {
        List<Session> sessionList = groupMemberInfoMap.computeIfAbsent(sid, k -> new ArrayList<>());
        Set<String> onlineUserList = onlineUserMap.computeIfAbsent(sid, k -> new HashSet<>());
        onlineUserList.add(userId);
        sessionList.add(session);

        // 发送上线通知
        sendInfo(sid, userId, onlineUserList.size(), "上线了~");

        log.info("Connection connected");
        log.info("sid: {}, sessionList size: {}", sid, sessionList.size());
    }


    public void sendInfo(String sid, String userId, Integer onlineSum, String info) {
        // 获取该连接用户信息
        BasUser currentUser = ApplicationContextUtil.getApplicationContext().getBean(BasUserMapper.class).selectById(userId);

        //BasUser currentUser = basUserService.getById(userId);
        // 发送通知
        MsgEntity msg = new MsgEntity();
        msg.setCount(onlineSum);
        msg.setUserId(userId);
        msg.setMsg(currentUser.getUserName() + info);
        // json对象转字符串
        String text = JSONObject.toJSONString(msg);
        onMessage(sid, userId, text);
    }

    /**
     * 关闭连接调用的方法，群成员退出
     *
     * @param session
     * @param sid
     */
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid, @PathParam("userId") String userId) {
        List<Session> sessionList = groupMemberInfoMap.get(sid);
        sessionList.remove(session);
        Set<String> onlineUserList = onlineUserMap.get(sid);
        onlineUserList.remove(userId);

        // 发送离线通知
        sendInfo(sid, userId, onlineUserList.size(), "下线了~");

        log.info("Connection closed");
        log.info("sid: {}, sessionList size: {}", sid, sessionList.size());
    }

    /**
     * 传输消息错误调用的方法
     *
     * @param error
     */
    @OnError
    public void OnError(Throwable error) {
        log.info("Connection error");
    }
}
