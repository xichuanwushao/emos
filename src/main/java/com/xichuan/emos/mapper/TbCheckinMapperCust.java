package com.xichuan.emos.mapper;


import com.xichuan.emos.domain.TbCheckin;

import java.util.HashMap;

public interface TbCheckinMapperCust {
    public Integer haveCheckin(HashMap param);
    public void insert(TbCheckin checkin);

}