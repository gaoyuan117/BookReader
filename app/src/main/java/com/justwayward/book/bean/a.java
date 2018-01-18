package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/25.
 */

public class a {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fjs : 3
         * img : img/jinfenghuang.png
         * name : 金凤凰
         * url : live/jinfenghuang.html
         */

        private String fjs;
        private String img;
        private String name;
        private String url;

        public String getFjs() {
            return fjs;
        }

        public void setFjs(String fjs) {
            this.fjs = fjs;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
