package com.direct.service.WXPayOrder;

import com.direct.model.WXPayOrder;

import java.util.List;

public interface OrderService {

    int insertSelective(WXPayOrder order);

    List<WXPayOrder> selectByOutTradeNo(String openid);

    List<WXPayOrder> selectByNotify(WXPayOrder order);

    //微信支付成功通知更新
    int updateByNotifySelective(WXPayOrder order);
}
