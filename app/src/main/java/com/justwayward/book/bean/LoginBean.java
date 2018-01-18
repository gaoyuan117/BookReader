package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/14.
 */

public class LoginBean {

    /**
     * uid : 3
     * nickname :
     * token : dcf4c9d7f727a773d772896e1ea1af5e84879d5940b51a312341cb6b7e0dbcdf
     */

    private int uid;
    private String nickname;
    private String token;
    private String create_time;
    private String expire_time;

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
