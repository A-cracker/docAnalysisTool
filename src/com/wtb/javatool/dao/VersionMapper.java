package com.wtb.javatool.dao;

import com.wtb.javatool.vo.Version;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface VersionMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into version(id,name,description) values(#{id},#{name},#{description})")
    void insertVersion(Version version);
    @Select("SELECT * FROM version AS t1 WHERE EXISTS (SELECT 1 FROM analysis_points AS t2 WHERE t2.fid = #{fid} AND t1.id = t2.versionId)")
    List<Version> selectVersionsByFid(@Param("fid")String fid);
    @Update("UPDATE version SET name = #{name}, description = #{description} WHERE id = #{id}")
    void updateVersion(@Param("id") int id, @Param("name") String name, @Param("description") String description);
    @Select("SELECT * FROM version where id = #{id} ")
    Version selectVersionById(@Param("id")int id);
    @Delete("delete from version WHERE id = #{versionId}")
    void deleteVersionById(@Param("versionId") int versionId);
    @Delete("delete t1 from analysis_content_matches t1 where exists (select 1 from analysis_points as t2 where versionId =#{versionId} and t1.id = t2.id)")
    void deleteACByVid(@Param("versionId") int versionId);
    @Delete("delete t1 from analysis_hierarchy t1 where exists (select 1 from analysis_points as t2 where versionId = #{versionId} and t1.cid = t2.id)")
    void deleteALsByVid(@Param("versionId") int versionId);
}
