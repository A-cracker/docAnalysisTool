package com.wtb.javatool.dao;

import com.fy.javanode.declarative.annotation.Param;
import com.wtb.javatool.vo.AnalysisLink;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AnalysisHierarchyMapper {
    @Insert("insert into analysis_hierarchy(cid,pid,layer,fid) values(#{cid},#{pid},#{layer},#{fid})")
    void insertAnalysisLink(AnalysisLink al);
    @Select("select cid,pid,layer,t1.fid as fid from analysis_hierarchy as t1 LEFT JOIN (SELECT id from analysis_points WHERE versionId = #{versionId}) as t2 on t1.cid = t2.id")
    List<AnalysisLink> selectAllLinksByVid( @Param("versionId") int versionId);
    @Delete("delete from analysis_hierarchy where fid = #{fid}")
    void deleteAnalysisHierarchy(@Param("fid")String fid);
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
