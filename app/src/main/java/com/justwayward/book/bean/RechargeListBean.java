package com.justwayward.book.bean;

/**
 * Created by gaoyuan on 2017/11/24.
 */

public class RechargeListBean {

    /**
     * id : 1
     * money : 20.00
     * coin : 2000
     * addcoin : 10
     * list_order : 1
     * add_time : 0
     */

    private int id;
    private String money;
    private int coin;
    private int addcoin;
    private int list_order;
    private int add_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getAddcoin() {
        return addcoin;
    }

    public void setAddcoin(int addcoin) {
        this.addcoin = addcoin;
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
