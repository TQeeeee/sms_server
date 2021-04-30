package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.BasMajor;
import com.example.sms_server.entity.display.DisplayMajor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BasMajorService extends IService<BasMajor> {
    public List<DisplayMajor> findAllDisplayMajor();
}
