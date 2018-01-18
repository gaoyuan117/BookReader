package com.justwayward.book.bean;

import java.io.Serializable;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class BookBean implements Serializable {

        private boolean member_switch;
        private boolean user_member;
        private int vip_read;
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
        private int prev_id;
        private int next_id;

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

        public int getVip_read() {
            return vip_read;
        }

        public void setVip_read(int vip_read) {
            this.vip_read = vip_read;
        }

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

        public int getPrev_id() {
            return prev_id;
        }

        public void setPrev_id(int prev_id) {
            this.prev_id = prev_id;
        }

        public int getNext_id() {
            return next_id;
        }

        public void setNext_id(int next_id) {
            this.next_id = next_id;
        }

}
