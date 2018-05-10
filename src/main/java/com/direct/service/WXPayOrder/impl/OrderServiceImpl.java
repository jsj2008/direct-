package com.direct.service.WXPayOrder.impl;

import com.direct.mapper.WXPayOrderMapper;
import com.direct.model.WXPayOrder;
import com.direct.service.WXPayOrder.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private WXPayOrderMapper orderMapper;

    @Override
    public int insertSelective(WXPayOrder order) {

        return orderMapper.insertSelective(order);
    }

    @Override
    public List<WXPayOrder> selectByOutTradeNo(String openid) {
        return orderMapper.selectByOutTradeNo(openid);
    }

    @Override
    public List<WXPayOrder> selectByNotify(WXPayOrder order) {
        return orderMapper.selectByNotify(order);
    }

    @Override
    public int updateByNotifySelective(WXPayOrder order) {
        return orderMapper.updateByNotifySelective(order);
    }
}
