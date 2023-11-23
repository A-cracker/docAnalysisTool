package com.wtb.javatool.util;


import java.util.ArrayList;
import java.util.List;


public class Node {
    private int pid;
    private int id;
    private List<Node> children;
    private String label;

    public Node(){}
    public Node(int id,int pid,String label) {
        this.id = id;
        this.pid = pid;
        this.label = label;
        this.children = new ArrayList<>();
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    public int getPid() {
        return pid;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
    public void setChildren(List<Node> children) {
        this.children = children;
    }
    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        children.add(child);
    }
}
