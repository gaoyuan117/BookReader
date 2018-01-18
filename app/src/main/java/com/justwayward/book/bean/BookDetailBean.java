package com.justwayward.book.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class BookDetailBean implements Serializable{


    /**
     * id : 1
     * title : 龙武战神
     * author : 罗玛
     * category_id : 1
     * category_name : 分类1
     * view_num : 1
     * collect_num : -8
     * word_num : 0
     * desc : 无数纪元百族争雄。一颗众生之心，诸族豪雄并起争锋。少年叶非习《血龙神术》，吞噬万物灵气崛起，杀遍诸界，得众生之心，浴血称尊。
     * pic : http://oz4nnu8m9.bkt.clouddn.com/FgoJLHSe3DdOEGdjul6SUuWlDAjq
     * status : 0
     * add_time : 1510653474
     * is_recommend : 0
     * is_bookshelf : 1
     * comment_list : [{"id":1,"uid":2,"user_nickname":"","avatar":"","novel_id":1,"title":"dfgdf","comment":"sdfsdf","score":1,"praise_num":0,"add_time":1510102407}]
     * guest_list : [{"id":1,"title":"龙武战神","author":"罗玛","pic":"http://oz4nnu8m9.bkt.clouddn.com/FgoJLHSe3DdOEGdjul6SUuWlDAjq","add_time":1510653474,"category_id":1,"category_name":"分类1"},{"id":2,"title":"超级魔法农场系统","author":"沧河贝壳","pic":"http://www.zwda.com/files/article/image/20/20960/20960s.jpg","add_time":1510653601,"category_id":1,"category_name":"分类1"}]
     * recommend_list : []
     */

    private int id;
    private String title;
    private String author;
    private int category_id;
    private String category_name;
    private String labels;
    private int view_num;
    private int collect_num;
    private String word_num;
    private String day_num;
    private String desc;
    private String pic;
    private int status;
    private int add_time;
    private int is_recommend;
    private int read_time;
    private int is_bookshelf;
    private boolean member_switch;
    private boolean user_member;
    private List<CommentListBean> comment_list;
    private List<GuestListBean> guest_list;
    private List<RecommendListBean> recommend_list;

    public int getRead_time() {
        return read_time;
    }

    public void setRead_time(int read_time) {
        this.read_time = read_time;
    }

    public String getLabels() {
        return labels;
    }

    public String getDay_num() {
        return day_num;
    }

    public void setDay_num(String day_num) {
        this.day_num = day_num;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public String getWord_num() {
        return word_num;
    }

    public void setWord_num(String word_num) {
        this.word_num = word_num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getIs_bookshelf() {
        return is_bookshelf;
    }

    public void setIs_bookshelf(int is_bookshelf) {
        this.is_bookshelf = is_bookshelf;
    }

    public List<CommentListBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentListBean> comment_list) {
        this.comment_list = comment_list;
    }

    public List<GuestListBean> getGuest_list() {
        return guest_list;
    }

    public void setGuest_list(List<GuestListBean> guest_list) {
        this.guest_list = guest_list;
    }

    public List<RecommendListBean> getRecommend_list() {
        return recommend_list;
    }

    public void setRecommend_list(List<RecommendListBean> recommend_list) {
        this.recommend_list = recommend_list;
    }

    public static class CommentListBean implements Serializable{
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

    public static class GuestListBean implements Serializable{
        /**
         * id : 1
         * title : 龙武战神
         * author : 罗玛
         * pic : http://oz4nnu8m9.bkt.clouddn.com/FgoJLHSe3DdOEGdjul6SUuWlDAjq
         * add_time : 1510653474
         * category_id : 1
         * category_name : 分类1
         */

        private int id;
        private String title;
        private String author;
        private String pic;
        private int add_time;
        private int category_id;
        private String category_name;

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

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }

    public static class RecommendListBean implements Serializable{
        /**
         * id : 1
         * title : 龙武战神
         * author : 罗玛
         * pic : http://oz4nnu8m9.bkt.clouddn.com/FgoJLHSe3DdOEGdjul6SUuWlDAjq
         * add_time : 1510653474
         * category_id : 1
         * category_name : 分类1
         */

        private int id;
        private String title;
        private String author;
        private String pic;
        private int add_time;
        private int category_id;
        private int word_num;
        private String category_name;

        public int getWord_num() {
            return word_num;
        }

        public void setWord_num(int word_num) {
            this.word_num = word_num;
        }

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

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }
}
