package com.wtb.javatool.dao;

import com.fy.javanode.declarative.annotation.Param;
import com.wtb.javatool.vo.AnalysisLink;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AnalysisHierarchyMapper {
    @Insert("insert into analysis_hierarchy(cid,pid,layer,fid) values(#{cid},#{pid},#{layer},#{fid})")
    void insertAnalysisLink(AnalysisLink al);
    @Select("select * from analysis_hierarchy where fid = #{fid}")
    List<AnalysisLink> selectAllLinksByFid(@Param("fid")String fid);
//    @Select("select min(pid) from analysis_hierarchy WHERE fid = #{fid}")
//    Integer selectRootByFid(@Param("fid")String fid);
    @Delete("delete from analysis_hierarchy where fid = #{fid}")
    int deleteAnalysisHierarchy(@Param("fid")String fid);
    @Select("select DISTINCT layer from analysis_hierarchy WHERE cid = #{cid}")
    Integer getLayerByCid(@Param("cid") int cid);
    @Delete("delete from analysis_hierarchy where cid = #{cid}")
    void deleteAlByCid(@Param("fid")int cid);
    @Delete("delete from analysis_hierarchy where pid = #{pid}")
    void deleteAlByPid(@Param("fid")int pid);
    @Select("select * from analysis_hierarchy where pid = #{pid}")
    List<AnalysisLink> getAlsByPid(@Param("fid")int pid);
    @Select("select max(layer) from analysis_hierarchy where fid = #{fid} LIMIT 1")
    Integer getTreeLayer(@Param("fid")String fid);
}
