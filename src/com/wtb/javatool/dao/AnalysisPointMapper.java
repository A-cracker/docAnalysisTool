package com.wtb.javatool.dao;

import com.wtb.javatool.vo.AnalysisPoint;
import org.apache.ibatis.annotations.*;


public interface AnalysisPointMapper {
     @Options(useGeneratedKeys = true, keyProperty = "id")
     @Insert("insert into analysis_points(id,name,fid,versionId) values(#{id},#{name},#{fid},#{versionId})")
     void insertAnalysisPoint(AnalysisPoint ap);
     @Select("select name from analysis_points where id = #{id}")
     String selectNameById(@Param("id") int id);
     @Select("select count(*) from analysis_points WHERE versionId = #{versionId}")
     Integer checkExist(@Param("versionId") int versionId);
     @Delete("delete from analysis_points WHERE fid = #{fid}")
     void deleteAnalysisPoint(@Param("fid") String fid);
     @Select("select min(id) from analysis_points WHERE fid = #{fid} and versionId = #{versionId}")
     Integer selectRootByFid(@Param("fid")String fid,@Param("versionId") int versionId);
     @Delete("delete from analysis_points where id = #{id}")
     void deleteApById(@Param("id") int id);
     @Update("update analysis_points set name = #{name} where id=#{id}")
     void updateAnalysisNameById(@Param("name") String name, @Param("id") int id);
     @Select("select max(versionId) from analysis_points where fid = #{fid}")
     Integer selectLatestVersion(@Param("fid")String fid);
     @Delete("delete from analysis_points where versionId = #{versionId}")
     void deleteAPsByVid(@Param("versionId") int versionId);
}
