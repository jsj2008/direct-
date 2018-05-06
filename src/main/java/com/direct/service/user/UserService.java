package com.direct.service.user;

import com.direct.model.User;

import java.util.List;

/**
 *
 */
public interface UserService {

    int addUser(User user);

    List<User> selectByOpenid(String openid);

    int updateByOpenidSelective(User user);

}
