package com.direct.mapper;

import com.direct.model.paragraphWz;

import java.util.List;

public interface paragraphWzMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(paragraphWz record);

    int insertSelective(paragraphWz record);

    paragraphWz selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(paragraphWz record);

    int updateByPrimaryKey(paragraphWz record);

    List<paragraphWz> selectAll();

    List<paragraphWz> selectPar();

}