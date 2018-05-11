package com.direct.mapper;

import com.direct.model.GameOrder;

import java.util.List;

public interface GameOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameOrder record);

    int insertSelective(GameOrder record);

    GameOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameOrder record);

    int updateByPrimaryKey(GameOrder record);

    List<GameOrder> selectByOutTradeNo(String orderid);

    int updateByNotifySelective(GameOrder gameOrder);
}