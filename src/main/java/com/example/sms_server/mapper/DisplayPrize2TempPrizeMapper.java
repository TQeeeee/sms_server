package com.example.sms_server.mapper;

import com.example.sms_server.entity.TempPrize;
import com.example.sms_server.entity.display.DisplayPrize;
import org.mapstruct.Mapper;

@Mapper
public interface DisplayPrize2TempPrizeMapper {
    TempPrize displayPrize2TempPrize(DisplayPrize displayPrize);
}
