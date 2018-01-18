package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class BannerBean {

    /**
     * id : 1
     * slide_id : 1
     * status : 1
     * list_order : 10000
     * title : 幻灯1
     * image : admin/20171006/a44612ade6c7d25b03dd72d8428a6157.jpg
     * url : #
     * target :
     * description : f
     * content : sgs
     * more : null
     */

    private int id;
    private int slide_id;
    private int status;
    private int list_order;
    private String title;
    private String image;
    private String image_url;
    private String url;
    private String target;
    private String description;
    private String content;
    private Object more;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlide_id() {
        return slide_id;
    }

    public void setSlide_id(int slide_id) {
        this.slide_id = slide_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getMore() {
        return more;
    }

    public void setMore(Object more) {
        this.more = more;
    }
}
