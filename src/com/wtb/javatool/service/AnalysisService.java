package com.wtb.javatool.service;

import com.wtb.javatool.util.Node;
import com.wtb.javatool.vo.AnalysisContent;
import com.wtb.javatool.vo.AnalysisLink;
import com.wtb.javatool.vo.AnalysisPoint;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public interface AnalysisService {
    public int extration(ArrayList<String> lines,String docId);
    public List<Node> bulidTree(int root,String fid);
    public List<AnalysisContent> getContentById(int id);
    public Integer getRootByFid(String fid);
    public Integer checkExist(String fid);
    public void deleteTree(String fid);
    public AnalysisPoint addAnalysisPoint(int pid,String fid, String name);
    public boolean deleteAnalysisPoint(int id,String fid,int root,int type);
    public int getTreeLayer(String fid);
    public void editAnalysisName(int id,String name);
}
