package com.direct.service.user.impl;

import com.direct.mapper.UserMapper;
import com.direct.model.User;
import com.direct.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {

        return userMapper.insertSelective(user);
    }

    @Override
    public List<User> selectByOpenid(String openid) {
        return userMapper.selectByOpenid(openid);
    }

    @Override
    public int updateByOpenidSelective(User user) {
        return userMapper.updateByOpenidSelective(user);
    }

}
