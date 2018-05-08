package com.direct.service.WXPayOrder;

import com.direct.model.WXPayOrder;

import java.util.List;

public interface OrderService {

    int insertSelective(WXPayOrder order);

    List<WXPayOrder> selectByOpenid(String openid);
}
