package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/12/7.
 */

public class FeedBackBean {


        /**
         * id : 1
         * type_name : 闪退
         * list_order : 1
         */

        private int id;
        private String type_name;
        private int list_order;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public int getList_order() {
            return list_order;
        }

        public void setList_order(int list_order) {
            this.list_order = list_order;
        }
}
