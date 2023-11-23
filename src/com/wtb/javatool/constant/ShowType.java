package com.wtb.javatool.constant;

import lombok.Getter;

@Getter
public enum ShowType {

    PRINT(1, "打印输出"),
    PUT(2, "放入语境"),
    LINK(3, "链接操作");

    private Integer type;
    private String name;

    ShowType(){}

    ShowType(Integer type, String name){
        this.type = type;
        this.name = name;
    }

}
