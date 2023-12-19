package com.wtb.javatool.service.impl;

import com.wtb.javatool.vo.Code;
import com.wtb.javatool.dao.CodeMapper;
import com.wtb.javatool.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    CodeMapper codeMapper;
    @Override
    public Integer addCode(String name,String author,int type,String date,String comment) {
        Code code = new Code();
        code.setName(name);
        code.setType(type);
        code.setAuthor(author);
        code.setDate(date);
        code.setComment(comment);
        codeMapper.insertCode(code);
        return code.getId();
    }
    @Override
    public boolean editCode(int id,String name,String author,int type,String date,String comment){
        codeMapper.updateCodeById(id, name, type, comment, author, date);
        return true;
    }
    @Override
    public List<Code> getAllCodes(){
        return codeMapper.selectAllCodes();
    }
    @Override
    public void deleteCodesByIdArr(List<String> idArr){
        for(String id : idArr){
            codeMapper.deleteCodeById(Integer.parseInt(id));
        }
    }
}
