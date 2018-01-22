package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/1/22.
 */

public class MyYuanListBean {

    /**
     * id : 1
     * uid : 3
     * novel : 圣域
     * add_time : 1516613821
     * source : [{"id":1,"uid":3,"novel_id":1,"url":"www.baidu.com","add_time":1516614494}]
     */

    private int id;
    private int uid;
    private String novel;
    private int add_time;
    private List<SourceBean> source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNovel() {
        return novel;
    }

    public void setNovel(String novel) {
        this.novel = novel;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public List<SourceBean> getSource() {
        return source;
    }

    public void setSource(List<SourceBean> source) {
        this.source = source;
    }

    public static class SourceBean {
        /**
         * id : 1
         * uid : 3
         * novel_id : 1
         * url : www.baidu.com
         * add_time : 1516614494
         */

        private int id;
        private int uid;
        private int novel_id;
        private String url;
        private int add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getNovel_id() {
            return novel_id;
        }

        public void setNovel_id(int novel_id) {
            this.novel_id = novel_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }
    }
}
