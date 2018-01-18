package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class ReviewDetailBean {

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
     * reply_list : [{"id":1,"novel_id":1,"comment_id":1,"uid":2,"user_nickname":"4dfg","avatar":"","content":"d4dddddddddfgdf","add_time":1510103404}]
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
    private int is_praise;
    private List<ReplyListBean> reply_list;

    public int getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(int is_praise) {
        this.is_praise = is_praise;
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

    public List<ReplyListBean> getReply_list() {
        return reply_list;
    }

    public void setReply_list(List<ReplyListBean> reply_list) {
        this.reply_list = reply_list;
    }

    public static class ReplyListBean {
        /**
         * id : 1
         * novel_id : 1
         * comment_id : 1
         * uid : 2
         * user_nickname : 4dfg
         * avatar :
         * content : d4dddddddddfgdf
         * add_time : 1510103404
         */

        private int id;
        private int novel_id;
        private int comment_id;
        private int uid;
        private String user_nickname;
        private String avatar;
        private String content;
        private int add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNovel_id() {
            return novel_id;
        }

        public void setNovel_id(int novel_id) {
            this.novel_id = novel_id;
        }

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }
    }
}
