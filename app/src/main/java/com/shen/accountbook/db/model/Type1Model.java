package com.shen.accountbook.db.model;

import java.util.List;

/**
 * 主类型的 javabean
 */
public class Type1Model {

        private String name;

        /** "主类型"的 javabean 构造函数*/
        public Type1Model() {
            super();
        }

        public Type1Model(String name) {
            super();
            this.name = name;
        }

        /** 得到"Type1类型"(String)*/
        public String getName() {
            return name;
        }

        /** 设置"Type1类型"*/
        public void setName(String name) {
            this.name = name;
        }



        @Override
        public String toString() {
            return "ProvinceModel [name=" + name  + "]";
        }
}
