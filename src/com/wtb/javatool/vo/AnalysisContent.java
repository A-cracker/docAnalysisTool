package com.wtb.javatool.vo;

import java.io.Serializable;

public class AnalysisContent implements Serializable {
    private int index;
    private int id;
    private int type;
    private String content;
    private String fid;
    public int getIndex(){
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {this.type = type;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFid(){ return fid; }
    public void setFid(String fid){ this.fid = fid;}

    @Override
    public String toString() {
        return "AnalysisContent{" +
                "id=" + id +
                ",type=" + type +
                ",content='"+ content +'\''+
                ",fid='"+ fid +'\''+
                '}';
    }
}
