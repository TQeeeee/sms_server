package com.example.sms_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sms_server.entity.TempPrize;


public interface TempPrizeService extends IService<TempPrize> {
    public void confirmTempById(String recordId);
}
