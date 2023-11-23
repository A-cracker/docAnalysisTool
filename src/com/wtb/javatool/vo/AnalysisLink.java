package com.wtb.javatool.vo;

public class AnalysisLink {
    private int cid;
    private int pid;
    private int layer;

    private String fid;
    public AnalysisLink(){}
    public AnalysisLink(int cid,int pid,int layer,String fid){
        this.cid = cid;
        this.pid = pid;
        this.layer = layer;
        this.fid = fid;
    }
    public int getCid() { return cid; }
    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPid() {return pid; }
    public void setPid(int pid) { this.pid = pid; }

    public int getLayer(){
        return layer;
    }
    public void setLayer(int layer) {
        this.layer = layer;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    @Override
    public String toString() {
        return "AnalysisLink{" +
                "cid=" + cid +
                ", pid=" + pid +
                ",layer="+ layer +
                ",fid='"+ fid + '\''+
                '}';
    }
}
