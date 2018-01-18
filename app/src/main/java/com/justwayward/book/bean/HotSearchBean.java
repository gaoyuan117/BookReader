package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class HotSearchBean {

    /**
     * total : 1
     * per_page : 5
     * current_page : 1
     * last_page : 1
     * data : [{"id":1,"searchword":"圣墟","num":1,"is_recommend":0,"update_time":1509675395}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * searchword : 圣墟
         * num : 1
         * is_recommend : 0
         * update_time : 1509675395
         */

        private int id;
        private String searchword;
        private int num;
        private int is_recommend;
        private int update_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSearchword() {
            return searchword;
        }

        public void setSearchword(String searchword) {
            this.searchword = searchword;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }
    }
}
