package com.direct.mapper;

import com.direct.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectByOpenid(String openid);
    //更新个人信息
    int updateByOpenidSelective(User record);
}