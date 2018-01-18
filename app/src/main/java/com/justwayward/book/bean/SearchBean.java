package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class SearchBean {

    /**
     * total : 1
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":41,"title":"圣墟","author":"辰东","pic":"http://www.dhzw.org/files/article/image/150/150037/150037s.jpg","view_num":0,"collect_num":0}]
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
         * id : 41
         * title : 圣墟
         * author : 辰东
         * pic : http://www.dhzw.org/files/article/image/150/150037/150037s.jpg
         * view_num : 0
         * collect_num : 0
         */

        private int id;
        private String title;
        private String author;
        private String pic;
        private int view_num;
        private int collect_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getView_num() {
            return view_num;
        }

        public void setView_num(int view_num) {
            this.view_num = view_num;
        }

        public int getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(int collect_num) {
            this.collect_num = collect_num;
        }
    }
}
