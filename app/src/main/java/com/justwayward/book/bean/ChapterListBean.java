package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class ChapterListBean {


    /**
     * id : 1
     * chapter : 第1章 好大一棵树
     * is_vip : 0
     * add_time : 1510653601
     */

    private int id;
    private String chapter;
    private int is_vip;
    private int add_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
