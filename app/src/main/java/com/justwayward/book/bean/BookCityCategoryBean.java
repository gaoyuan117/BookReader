package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/23.
 */

public class BookCityCategoryBean {

    /**
     * id : 6
     * category : 都市
     * pid : 1
     * is_recommend : 1
     * list_order : 0
     */

    private String id;
    private String category;
    private int pid;
    private int is_recommend;
    private int list_order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }
}
