package com.wtb.javatool.service;

import com.wtb.javatool.vo.Code;

import java.util.List;

public interface CodeService {
    public Integer addCode(String name,String author,int type,String date,String comment);
    public boolean editCode(int id,String name,String author,int type,String date,String comment);
    public List<Code> getAllCodes();
    public void deleteCodesByIdArr(List<String> idArr);
    public void updateProps(int id,String props);
    public void updateFuncs(int id,String funcs);
}
