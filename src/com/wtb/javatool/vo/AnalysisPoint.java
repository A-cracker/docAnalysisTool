package com.wtb.javatool.vo;

import java.io.Serializable;

public class AnalysisPoint implements Serializable {
    private int id;
    private String name;
    private String fid;
    private int versionId;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFid(){ return fid; }
    public void setFid(String fid) { this.fid = fid;}
    public int getVersionId() {
        return versionId;
    }
    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }
    @Override
    public String toString() {
        return "AnalysisPoint{" +
                "id=" + id +
                ",name='" + name + '\'' +
                ",fid='" + fid + '\'' +
                ",versionId=" + versionId +
                '}';
    }
}
