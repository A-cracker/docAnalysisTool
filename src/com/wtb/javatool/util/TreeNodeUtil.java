package com.wtb.javatool.util;

import java.util.LinkedList;
import java.util.List;
import com.wtb.javatool.util.Node;
public class TreeNodeUtil {
    public static List<Node> treeBuild(List<Node> treeNodeList, long pid){
        List<Node> list=new LinkedList<Node>();
        for (Node node:treeNodeList){
            if (node.getPid()==pid){
                node.setChildren(treeBuild(treeNodeList,node.getId()));
                list.add(node);
            }
        }
        return list;
    }

}
