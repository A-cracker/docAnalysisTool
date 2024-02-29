package com.wtb.javatool.dao;

import com.wtb.javatool.vo.Code;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface CodeMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into code(id,name,type,comment,author,date,callers,props,functions,callees) values(#{id},#{name},#{type},#{comment},#{author},#{date},#{callers},#{props},#{functions},#{callees})")
    void insertCode(Code code);
    @Update("update code set name = #{name},type = #{type},author = #{author},date = #{date},comment = #{comment} where id=#{id}")
    void updateCodeById(@Param("id") int id,@Param("name")String name,@Param("type")int type,@Param("comment")String comment,@Param("author")String author,@Param("date")String date);
    @Select("select * from code")
    List<Code> selectAllCodes();
    @Delete("delete from code where id=#{id}")
    void deleteCodeById(@Param("id") int id);
    @Update("update code set props=#{props} where id=#{id}")
    void updateProps(@Param("props")String props,@Param("id") int id);
    @Update("update code set functions=#{funcs} where id=#{id}")
    void updateFuncs(@Param("funcs")String funcs,@Param("id") int id);
}
