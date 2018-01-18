package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/23.
 */

public class CategoryBean {

    /**
     * id : 1
     * category : 分类1
     * pid : 0
     * list_order : 0
     * sub_category : [{"id":2,"category":"测试分类","pid":1,"list_order":0,"novel_num":0}]
     */

    private int id;
    private String category;
    private int pid;
    private int list_order;
    private List<SubCategoryBean> sub_category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

    public List<SubCategoryBean> getSub_category() {
        return sub_category;
    }

    public void setSub_category(List<SubCategoryBean> sub_category) {
        this.sub_category = sub_category;
    }

    public static class SubCategoryBean {
        /**
         * id : 2
         * category : 测试分类
         * pid : 1
         * list_order : 0
         * novel_num : 0
         */

        private int id;
        private String category;
        private int pid;
        private int list_order;
        private int novel_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getList_order() {
            return list_order;
        }

        public void setList_order(int list_order) {
            this.list_order = list_order;
        }

        public int getNovel_num() {
            return novel_num;
        }

        public void setNovel_num(int novel_num) {
            this.novel_num = novel_num;
        }
    }
}
