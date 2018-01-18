package com.justwayward.book.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyuan on 2017/12/7.
 */

public class SourceBean implements Serializable{
    /**
     * total : 1
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":1,"site_name":"玄幻小说_好看的玄幻小说_玄幻小说排行榜_大海中","site_url":"www.dhzw.org","site_icon":""}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * site_name : 玄幻小说_好看的玄幻小说_玄幻小说排行榜_大海中
         * site_url : www.dhzw.org
         * site_icon :
         */

        private int id;
        private String site_name;
        private String site_url;
        private String site_icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSite_name() {
            return site_name;
        }

        public void setSite_name(String site_name) {
            this.site_name = site_name;
        }

        public String getSite_url() {
            return site_url;
        }

        public void setSite_url(String site_url) {
            this.site_url = site_url;
        }

        public String getSite_icon() {
            return site_icon;
        }

        public void setSite_icon(String site_icon) {
            this.site_icon = site_icon;
        }
    }
}
