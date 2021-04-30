package com.example.sms_server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sms_server.entity.BasStudent;
import com.example.sms_server.entity.TempPrize;
import com.example.sms_server.mapper.BasStudentMapper;
import com.example.sms_server.mapper.TempPrizeMapper;
import com.example.sms_server.service.TempPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TempPrizeServiceImpl extends ServiceImpl<TempPrizeMapper, TempPrize> implements TempPrizeService {
    @Autowired
    TempPrizeMapper tempPrizeMapper;
    @Autowired
    BasStudentMapper basStudentMapper;
    @Override
    public void confirmTempById(String recordId) {
        //将该记录的字段置为1，并且，将奖惩记录内容追加到student表的该记录末尾
        TempPrize tempPrize = tempPrizeMapper.selectById(recordId);
        tempPrize.setConfirmed(Integer.parseInt("1"));
        BasStudent basStudent = basStudentMapper.selectById(tempPrize.getStudentId());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(basStudent.getPrizeRecord().trim());
        stringBuffer.append(";;");
        stringBuffer.append(tempPrize.getPrizeRecord());
        basStudent.setPrizeRecord(stringBuffer.toString());
        // 更新tempPrize和basStudent
        tempPrizeMapper.updateById(tempPrize);
        basStudentMapper.updateById(basStudent);

    }
}
