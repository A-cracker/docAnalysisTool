package com.wtb.javatool.service;

import com.wtb.javatool.util.Node;
import com.wtb.javatool.vo.AnalysisContent;
import com.wtb.javatool.vo.AnalysisLink;
import com.wtb.javatool.vo.AnalysisPoint;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AnalysisService {
    public Map<String,Integer> extraction(ArrayList<String> lines, String docId);
    public List<Node> bulidTree(int root,String fid,int versionId);
    public List<AnalysisContent> getContentById(int id);
    public Integer getRootByFid(String fid,int versionId);
    public Integer checkExist(int versionId);
    public void deleteTree(String fid);
    public AnalysisPoint addAnalysisPoint(int pid,String fid, String name,int versionId);
    public boolean deleteAnalysisPoint(int id,String fid,int root,int type,int versionId);
    public int getTreeLayer(String fid);
    public void editAnalysisName(int id,String name);

}
