package com.wtb.javatool.constant;

import lombok.Getter;

@Getter
public enum ContentType {

    TABLE(0, "表格"),
    IMAGE(1, "图片"),
    TEXT(2, "文本");

    private Integer type;
    private String name;

    ContentType(){}

    ContentType(Integer type, String name){
        this.type = type;
        this.name = name;
    }

}