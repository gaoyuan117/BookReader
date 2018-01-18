package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/24.
 */

public class RechargeRecordBean {

    /**
     * month : 2017年11月
     * list : [{"id":1,"uid":2,"pay_type":"alipay","pay_money":"10.00","status":0,"add_time":1510643367}]
     */

    private String month;
    private List<ListBean> list;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * uid : 2
         * pay_type : alipay
         * pay_money : 10.00
         * status : 0
         * add_time : 1510643367
         */

        private int id;
        private int uid;
        private String pay_type;
        private String pay_money;
        private int status;
        private long add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getPay_money() {
            return pay_money;
        }

        public void setPay_money(String pay_money) {
            this.pay_money = pay_money;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }
    }
}
