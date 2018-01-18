package com.justwayward.book.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class BookShelfBean implements Serializable {

    /**
     * total : 1
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":21,"uid":5,"novel_id":4,"novel_name":"龙印神皇","is_top":0,"update_time":0,"read_time":0,"add_time":1512046687,"title":"龙印神皇","pic":"http://oz4nnu8m9.bkt.clouddn.com/Fq6U84BzYSK5bncvc-W6NBovmqR3","author":"我是大乌龟","desc":"【热血新玄幻，百万读者强烈推荐！】八荒乱，星辰动，少年意外夺取天龙至尊记忆，从卑微之处如彗星崛起！展开了一段，与万千天骄，诸天神魔，争霸天下的热血传奇！铭刻最强龙印，传承远古神体，一手扭转乾坤，一念毁灭星辰！踏累累白骨，终成一代神皇！","chapter_info":{"chapter":"","is_vip":0,"add_time":0}}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private boolean member_switch;
    private boolean user_member;
    private List<DataBean> data;

    public boolean isMember_switch() {
        return member_switch;
    }

    public void setMember_switch(boolean member_switch) {
        this.member_switch = member_switch;
    }

    public boolean isUser_member() {
        return user_member;
    }

    public void setUser_member(boolean user_member) {
        this.user_member = user_member;
    }

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
         * id : 21
         * uid : 5
         * novel_id : 4
         * novel_name : 龙印神皇
         * is_top : 0
         * update_time : 0
         * read_time : 0
         * add_time : 1512046687
         * title : 龙印神皇
         * pic : http://oz4nnu8m9.bkt.clouddn.com/Fq6U84BzYSK5bncvc-W6NBovmqR3
         * author : 我是大乌龟
         * desc : 【热血新玄幻，百万读者强烈推荐！】八荒乱，星辰动，少年意外夺取天龙至尊记忆，从卑微之处如彗星崛起！展开了一段，与万千天骄，诸天神魔，争霸天下的热血传奇！铭刻最强龙印，传承远古神体，一手扭转乾坤，一念毁灭星辰！踏累累白骨，终成一代神皇！
         * chapter_info : {"chapter":"","is_vip":0,"add_time":0}
         */

        private int id;
        private int uid;
        private int novel_id;
        private String novel_name;
        private int is_top;
        private int update_time;
        private int read_time;
        private int add_time;
        private int ad_position;
        private int is_update;
        private String title;
        private String pic;
        private String author;
        private String desc;
        private String type;

        public int getIs_update() {
            return is_update;
        }

        public void setIs_update(int is_update) {
            this.is_update = is_update;
        }

        private ChapterInfoBean chapter_info;

        public int getAd_position() {
            return ad_position;
        }

        public void setAd_position(int ad_position) {
            this.ad_position = ad_position;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

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

        public String getNovel_name() {
            return novel_name;
        }

        public void setNovel_name(String novel_name) {
            this.novel_name = novel_name;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public int getRead_time() {
            return read_time;
        }

        public void setRead_time(int read_time) {
            this.read_time = read_time;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public ChapterInfoBean getChapter_info() {
            return chapter_info;
        }

        public void setChapter_info(ChapterInfoBean chapter_info) {
            this.chapter_info = chapter_info;
        }

        public static class ChapterInfoBean {
            /**
             * chapter :
             * is_vip : 0
             * add_time : 0
             */

            private String chapter;
            private int is_vip;
            private int add_time;

            public String getChapter() {
                return chapter;
            }

            public void setChapter(String chapter) {
                this.chapter = chapter;
            }

            public int getIs_vip() {
                return is_vip;
            }

            public void setIs_vip(int is_vip) {
                this.is_vip = is_vip;
            }

            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }
        }
    }
}
