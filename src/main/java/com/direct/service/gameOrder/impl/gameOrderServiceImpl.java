package com.direct.service.gameOrder.impl;

import com.direct.mapper.GameOrderMapper;
import com.direct.model.GameOrder;
import com.direct.service.gameOrder.gameOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "gameOrderService")
public class gameOrderServiceImpl implements gameOrderService {

    @Autowired
    private GameOrderMapper gameOrderMapper;

    @Override
    public int insertSelective(GameOrder gameOrder) {

        return gameOrderMapper.insertSelective(gameOrder);
    }

    @Override
    public List<GameOrder> selectByOutTradeNo(String orderid) {
        return gameOrderMapper.selectByOutTradeNo(orderid);
    }

    @Override
    public int updateByNotifySelective(GameOrder gameOrder) {
        return gameOrderMapper.updateByNotifySelective(gameOrder);
    }


}
