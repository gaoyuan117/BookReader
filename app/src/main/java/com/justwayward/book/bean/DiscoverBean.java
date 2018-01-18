package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class DiscoverBean {

    /**
     * id : 1
     * zone_name : 听书专区
     * zone_link : http://www.baidu.com
     * icon : http://oz4nnu8m9.bkt.clouddn.com/admin/20171125/f586228585bf6d04d02b2d9de42f8dab.jpg!watermark
     * list_order : 54
     * add_time : 1511831313
     */

    private int id;
    private String zone_name;
    private String zone_link;
    private String icon;
    private int list_order;
    private int add_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getZone_link() {
        return zone_link;
    }

    public void setZone_link(String zone_link) {
        this.zone_link = zone_link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
