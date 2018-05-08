package com.direct.mapper;

import com.direct.model.WXPayOrder;

import java.util.List;

public interface WXPayOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WXPayOrder record);

    int insertSelective(WXPayOrder record);

    WXPayOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WXPayOrder record);

    int updateByPrimaryKey(WXPayOrder record);

    List<WXPayOrder> selectByOpenid(String openid);
}