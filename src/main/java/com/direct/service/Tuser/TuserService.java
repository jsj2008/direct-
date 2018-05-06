package com.direct.service.Tuser;

import com.direct.model.Tuser;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
public interface TuserService {

    int addUser(Tuser user);

    List<Tuser> findAllUser(int pageNum, int pageSize);
}
