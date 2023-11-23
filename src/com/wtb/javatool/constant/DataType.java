package com.wtb.javatool.constant;

import lombok.Getter;

@Getter
public enum DataType {

    STR(1, "string类型"),
    LIST(2, "list类型"),
    MAP(3, "map类型");

    private Integer type;
    private String name;

    DataType(){}

    DataType(Integer type, String name){
        this.type = type;
        this.name = name;
    }

}
