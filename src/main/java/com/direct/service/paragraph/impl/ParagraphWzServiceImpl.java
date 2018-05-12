package com.direct.service.paragraph.impl;

import com.direct.mapper.paragraphWzMapper;
import com.direct.model.paragraphWz;
import com.direct.service.paragraph.ParagraphWzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "paragraphWzService")
public class ParagraphWzServiceImpl implements ParagraphWzService {

    @Autowired
    private paragraphWzMapper paragraphWzMapper;

    @Override
    public List<paragraphWz> selectAll() {
        return paragraphWzMapper.selectAll();
    }

    @Override
    public List<paragraphWz> selectPar() {
        return paragraphWzMapper.selectPar();
    }
}
