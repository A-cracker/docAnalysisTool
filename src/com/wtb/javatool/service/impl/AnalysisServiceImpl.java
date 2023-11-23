package com.wtb.javatool.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wtb.javatool.constant.ContentType;
import com.wtb.javatool.dao.*;
import com.wtb.javatool.service.AnalysisService;
import com.wtb.javatool.util.TreeNodeUtil;
import com.wtb.javatool.vo.AnalysisContent;
import com.wtb.javatool.vo.AnalysisLink;
import com.wtb.javatool.vo.AnalysisPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wtb.javatool.util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Autowired
    private AnalysisPointMapper analysisPointMapper;
    @Autowired
    private AnalysisHierarchyMapper analysisHierarchyMapper;
    @Autowired
    private AnalysisContentMapper analysisContentMapper;
    @Override
    public int extration(ArrayList<String> lines, String docId) {
        int currentId = 0;
        int root = 0;
        int currentLayer=0;
        int aid;
        HashMap<Integer, Integer> map = new HashMap<>();//记录当前层数对应的分析点id

        String regex = "\\{([^}]+)\\} \\{([^}]+)\\} (.+)";
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(regex);

        for (String line : lines) {
            // 创建 Matcher 对象
//            System.out.println("执行了ex");
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()){//检查数据是否符合要抽取的格式
                String type = matcher.group(1).replace("'", "");
                String docType = matcher.group(2).replace("'", "");
                String content = matcher.group(3);

                if(docType.equals("Heading 0")){
                    //如果是第一层文件名
                    AnalysisPoint ap = new AnalysisPoint();
                    ap.setName(content);
                    ap.setFid(docId);
                    analysisPointMapper.insertAnalysisPoint(ap);
                    root = ap.getId();
                    System.out.println(root);
                    currentId = root;
                    currentLayer = 1;
                    map.put(currentLayer,root);
                    AnalysisLink al = new AnalysisLink();
                    al.setCid(root);
                    al.setLayer(1);
                    al.setPid(0);
                    al.setFid(docId);
                    analysisHierarchyMapper.insertAnalysisLink(al);
                }else { //如果不是第一层
                    if (docType.startsWith("Heading")) {//如果是标题，则将其作为分析点
                        AnalysisPoint ap = new AnalysisPoint();
                        ap.setName(content);
                        ap.setFid(docId);
                        analysisPointMapper.insertAnalysisPoint(ap);
                        aid = ap.getId();//获取当前分析点id
                        AnalysisLink al = new AnalysisLink();
                        currentId = aid;
                        al.setCid(aid);//设置cid为当前的aid
                        currentLayer = Integer.parseInt(docType.split(" ")[1])+1;
                        al.setLayer(currentLayer);
                        map.put(currentLayer,aid);//更新追踪表
                        al.setPid(map.get(currentLayer-1));
                        al.setFid(docId);
                        analysisHierarchyMapper.insertAnalysisLink(al);
                    }else {//如果不是标题
                        AnalysisContent ac = new AnalysisContent();
                        ac.setId(currentId);//设置当前匹配的aid
                        if (type.equals("image")) {
                            ac.setType(ContentType.IMAGE.getType());
                        } else if (type.equals("text")) {
                            ac.setType(ContentType.TEXT.getType());
                        } else if (type.equals("table")) {
                            ac.setType(ContentType.TABLE.getType());
                        }
                        ac.setFid(docId);
                        ac.setContent(content);
                        analysisContentMapper.insertAnalysisContent(ac);
                    }
                }
            }
        }
        return root;
    }
    @Override
    public List<Node> bulidTree(int root,String fid){
        List<AnalysisLink> links= analysisHierarchyMapper.selectAllLinksByFid(fid);
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(root,0,analysisPointMapper.selectNameById(root)));
        for(AnalysisLink link : links){
            nodes.add(new Node(link.getCid(),link.getPid(),analysisPointMapper.selectNameById(link.getCid())));
        }
        return TreeNodeUtil.treeBuild(nodes,0);
    };
    @Override
    public List<AnalysisContent> getContentById(int id){
        return analysisContentMapper.selectContentById(id);
    }
    @Override
    public Integer getRootByFid(String fid){
        return analysisPointMapper.selectRootByFid(fid);
    }
    @Override
    public Integer checkExist(String fid){
        return analysisPointMapper.checkExist(fid);
    }
    @Override
    public void deleteTree(String fid){
        analysisHierarchyMapper.deleteAnalysisHierarchy(fid);
        analysisContentMapper.deleteAnalysisContent(fid);
        analysisPointMapper.deleteAnalysisPoint(fid);
    }
    @Override
    public AnalysisPoint addAnalysisPoint(int pid, String fid, String name){
        AnalysisPoint ap = new AnalysisPoint();
        ap.setName(name);
        ap.setFid(fid);
        analysisPointMapper.insertAnalysisPoint(ap);
        AnalysisLink al = new AnalysisLink();
        al.setPid(pid);
        al.setCid(ap.getId());
        al.setLayer(analysisHierarchyMapper.getLayerByCid(pid)+1);
        al.setFid(fid);
        analysisHierarchyMapper.insertAnalysisLink(al);
        return ap;
    }
    @Override
    public boolean deleteAnalysisPoint(int id,String fid,int root,int type){
        if(id == root){
            return false;//不允许删除根节点
        }else {
            if(analysisHierarchyMapper.getAlsByPid(id).size() != 0){
                if(type == 0) {//如果是保留节点内容
                    AnalysisPoint ap = new AnalysisPoint();
                    ap.setName("新增分析点");
                    ap.setFid(fid);
                    analysisPointMapper.insertAnalysisPoint(ap);
                    AnalysisLink al = new AnalysisLink();
                    al.setCid(ap.getId());
                    al.setFid(fid);
                    al.setPid(root);
                    al.setLayer(2);
                    analysisHierarchyMapper.insertAnalysisLink(al);
                    List<AnalysisLink> links = analysisHierarchyMapper.getAlsByPid(id);
                    //把原先的节点挂在新增节点上
                    for (AnalysisLink link : links) {
                        analysisHierarchyMapper.deleteAlByCid(link.getCid());
                        analysisHierarchyMapper.insertAnalysisLink(new AnalysisLink(link.getCid(), ap.getId(), 3, fid));
                    }
                }else{
                    deleteTree(id);
                }
            }
            analysisHierarchyMapper.deleteAlByCid(id);
            analysisContentMapper.deleteAcById(id);
            analysisPointMapper.deleteApById(id);
            return true;
        }
    }
    public void deleteTree(int id){
        List<AnalysisLink> links = analysisHierarchyMapper.getAlsByPid(id);
        for (AnalysisLink link : links) {
            int cid = link.getCid();
            deleteTree(cid);
            analysisHierarchyMapper.deleteAlByCid(link.getCid());
            analysisContentMapper.deleteAcById(cid);
        }
    }
}


