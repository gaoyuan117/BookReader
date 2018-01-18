package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/28.
 */

public class UserBean {

    /**
     * id : 3
     * avatar :
     * user_nickname : 13220122946
     * sex : 0
     * mobile : 13220122946
     * coin : 0
     * expire_time : 0
     * like_type : 0
     * bookshelf_sort : update
     */

    private int id;
    private String avatar;
    private String user_nickname;
    private int sex;
    private String mobile;
    private int coin;
    private long expire_time;
    private long create_time;
    private int like_type;
    private String bookshelf_sort;

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public int getLike_type() {
        return like_type;
    }

    public void setLike_type(int like_type) {
        this.like_type = like_type;
    }

    public String getBookshelf_sort() {
        return bookshelf_sort;
    }

    public void setBookshelf_sort(String bookshelf_sort) {
        this.bookshelf_sort = bookshelf_sort;
    }
}
