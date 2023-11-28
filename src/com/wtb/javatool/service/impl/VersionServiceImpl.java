package com.wtb.javatool.service.impl;

import com.wtb.javatool.dao.AnalysisPointMapper;
import com.wtb.javatool.dao.VersionMapper;
import com.wtb.javatool.service.VersionService;
import com.wtb.javatool.vo.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionServiceImpl implements VersionService {
    @Autowired
    AnalysisPointMapper analysisPointMapper;
    @Autowired
    VersionMapper versionMapper;
    @Override
    public Integer getLatestVersionByFid(String fid){
        return analysisPointMapper.selectLatestVersion(fid);
    }
    @Override
    public List<Version> getVersionsByFid(String fid){
        return versionMapper.selectVersionsByFid(fid);
    }
    @Override
    public boolean editVersion(int versionId,String name,String description){
        versionMapper.updateVersion(versionId,name,description);
        return true;
    }
    @Override
    public Version getVersionById(int versionId){
        return versionMapper.selectVersionById(versionId);
    }
    @Override
    public boolean deleteVersion(int versionId){
        versionMapper.deleteACByVid(versionId);
        versionMapper.deleteALsByVid(versionId);
        analysisPointMapper.deleteAPsByVid(versionId);
        versionMapper.deleteVersionById(versionId);
        return true;
    }
    @Override
    public boolean deleteHistoryVersion(int versionId){
        versionMapper.deleteACByVid(versionId);
        versionMapper.deleteALsByVid(versionId);
        analysisPointMapper.deleteAPsByVid(versionId);
        return true;
    }
}
