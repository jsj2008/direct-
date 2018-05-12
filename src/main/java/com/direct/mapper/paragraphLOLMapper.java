package com.direct.mapper;

import com.direct.model.paragraphLOL;

public interface paragraphLOLMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(paragraphLOL record);

    int insertSelective(paragraphLOL record);

    paragraphLOL selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(paragraphLOL record);

    int updateByPrimaryKey(paragraphLOL record);
}