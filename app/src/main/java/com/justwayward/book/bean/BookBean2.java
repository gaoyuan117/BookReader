package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/29.
 */

public class BookBean2 {

    /**
     * code : 200
     * message : 操作成功
     * data : {"id":53,"novel_id":2,"source_id":0,"source_url":"","chapter":"第一章ddfgfdg","content":"&lt;p&gt;sdfsdf&lt;/p&gt;","word_num":22,"is_vip":0,"list_order":2,"add_time":1511599215}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 53
         * novel_id : 2
         * source_id : 0
         * source_url :
         * chapter : 第一章ddfgfdg
         * content : &lt;p&gt;sdfsdf&lt;/p&gt;
         * word_num : 22
         * is_vip : 0
         * list_order : 2
         * add_time : 1511599215
         */

        private int id;
        private int novel_id;
        private int source_id;
        private String source_url;
        private String chapter;
        private String content;
        private int word_num;
        private int is_vip;
        private int list_order;
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

        public int getSource_id() {
            return source_id;
        }

        public void setSource_id(int source_id) {
            this.source_id = source_id;
        }

        public String getSource_url() {
            return source_url;
        }

        public void setSource_url(String source_url) {
            this.source_url = source_url;
        }

        public String getChapter() {
            return chapter;
        }

        public void setChapter(String chapter) {
            this.chapter = chapter;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getWord_num() {
            return word_num;
        }

        public void setWord_num(int word_num) {
            this.word_num = word_num;
        }

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }

        public int getList_order() {
            return list_order;
        }

        public void setList_order(int list_order) {
            this.list_order = list_order;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }
    }
}
