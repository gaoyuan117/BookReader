package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/12/2.
 */

public class RechargeMoneyBean {

    /**
     * code : 200
     * message : 操作成功
     * data : ["5","10"]
     */

    private int code;
    private String message;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
