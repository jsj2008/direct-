package com.direct.mapper;

import com.direct.model.Tuser;

import java.util.List;

public interface TuserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(Tuser record);

    int insertSelective(Tuser record);

    Tuser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(Tuser record);

    int updateByPrimaryKey(Tuser record);

    List<Tuser> selectAllUser();
}