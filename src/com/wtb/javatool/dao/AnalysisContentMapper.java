package com.wtb.javatool.dao;

import com.fy.javanode.declarative.annotation.Param;
import com.wtb.javatool.vo.AnalysisContent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AnalysisContentMapper {
    @Insert("insert into analysis_content_matches(id,type,content,fid) values(#{id},#{type},#{content},#{fid})")
    void insertAnalysisContent(AnalysisContent ac);
    //根据节点id获取相应的内容
    @Select("select * from analysis_content_matches where id = #{id}")
    List<AnalysisContent> selectContentById(@Param("id") int id);
    @Delete("delete from analysis_content_matches WHERE fid = #{fid}")
    int deleteAnalysisContent(@Param("fid") String fid);
    @Delete("delete from analysis_content_matches WHERE id = #{id}")
    void deleteAcById(@Param("id") int id);
}
