package com.tr.datakeeper.utils;

import com.tr.datakeeper.dto.DataDto;
import com.tr.datakeeper.entity.Data;
import org.springframework.beans.BeanUtils;

public class ConvertUtils {

    public static DataDto entityToDto(Data data){
        DataDto dto = new DataDto();
        BeanUtils.copyProperties(data, dto);
        return dto;
    }

    public static Data dtoToEntity(DataDto dto){
        Data data = new Data();
        BeanUtils.copyProperties(dto, data);
        return data;
    }

}
