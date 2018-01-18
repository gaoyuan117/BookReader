package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class ReviewBean {

    /**
     * total : 2
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":1,"uid":2,"user_nickname":"","avatar":"","novel_id":1,"title":"dfgdf","comment":"sdfsdf","score":1,"praise_num":0,"add_time":1510102407},{"id":2,"uid":2,"user_nickname":"4dfg","avatar":"","novel_id":1,"title":"dfgdf","comment":"sdfsdf","score":1,"praise_num":0,"add_time":1510103362}]
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
         * uid : 2
         * user_nickname :
         * avatar :
         * novel_id : 1
         * title : dfgdf
         * comment : sdfsdf
         * score : 1
         * praise_num : 0
         * add_time : 1510102407
         */

        private int id;
        private int uid;
        private String user_nickname;
        private String avatar;
        private int novel_id;
        private String title;
        private String comment;
        private int score;
        private int praise_num;
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

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getNovel_id() {
            return novel_id;
        }

        public void setNovel_id(int novel_id) {
            this.novel_id = novel_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(int praise_num) {
            this.praise_num = praise_num;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }
    }
}
