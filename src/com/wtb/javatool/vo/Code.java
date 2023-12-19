package com.wtb.javatool.vo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Code implements Serializable {
        @Getter
        @Setter
        private int id;
        @Getter
        @Setter
        private String name;
        @Getter
        @Setter
        private int type;
        @Getter
        @Setter
        private String comment;
        @Getter
        @Setter
        private String author;
        @Getter
        @Setter
        private String date;
        @Getter
        @Setter
        //Json序列化String
        private String props;
        @Getter
        @Setter
        //Json序列化String
        private String callers;
        @Getter
        @Setter
        //Json序列化String
        private String functions;
        @Getter
        @Setter
        //Json序列化String
        private String callees;
        public Code(){
                this.comment = "";
                this.author = "";
                this.date = "";
                this.props = "[]";
                this.callers = "[]";
                this.functions = "[]";
                this.callees = "[]";
        }
}
