package com.wtb.javatool.service;

import com.wtb.javatool.vo.Version;

import java.util.List;

public interface VersionService {
    public Integer getLatestVersionByFid(String fid);
    public List<Version> getVersionsByFid(String fid);
    public boolean editVersion(int versionId,String name,String description);
    public Version getVersionById(int versionId);
    public boolean deleteVersion(int versionId);
}
