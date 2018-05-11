package com.direct.service.gameOrder;

import com.direct.model.GameOrder;

import java.util.List;

public interface gameOrderService {

    int insertSelective(GameOrder gameOrder);

    List<GameOrder> selectByOutTradeNo(String orderid);

    //微信支付成功通知更新
    int updateByNotifySelective(GameOrder gameOrder);

}
