package com.justwayward.book.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/27.
 */

public class CategoryListBean {

    /**
     * total : 1
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * data : [{"id":1,"title":"金枝夙孽","author":"籽日","category_id":6,"view_num":0,"collect_num":0,"word_num":0,"desc":"    他说，\u201c我已是王爷，我能给你想要的一切。\u201d    她答，\u201c不要喜欢我，我不只是文无忧。    我也不会喜欢你，我不曾有那种情愫。\u201d    她是万千离弦的箭，穿过层台漫月，烟笼寒水，只向仇人的心。    她是万滴轻柔的雨，千娇百媚，回望倾城，只向助她功成之人、奉献一切。   ","pic":"http://oz4nnu8m9.bkt.clouddn.com/FpYkJoQgbVnAnY0zN01ZayywnWgs","status":1,"labels":"","add_time":1511256087,"is_recommend":0,"main_recommend":0,"type":"collect","is_freelimit":0}]
     */
    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 金枝夙孽
         * author : 籽日
         * category_id : 6
         * view_num : 0
         * collect_num : 0
         * word_num : 0
         * desc :     他说，“我已是王爷，我能给你想要的一切。”    她答，“不要喜欢我，我不只是文无忧。    我也不会喜欢你，我不曾有那种情愫。”    她是万千离弦的箭，穿过层台漫月，烟笼寒水，只向仇人的心。    她是万滴轻柔的雨，千娇百媚，回望倾城，只向助她功成之人、奉献一切。   
         * pic : http://oz4nnu8m9.bkt.clouddn.com/FpYkJoQgbVnAnY0zN01ZayywnWgs
         * status : 1
         * labels :
         * add_time : 1511256087
         * is_recommend : 0
         * main_recommend : 0
         * type : collect
         * is_freelimit : 0
         */

        private int id;
        private String title;
        private String author;
        private int category_id;
        private int view_num;
        private int collect_num;
        private int word_num;
        private String desc;
        private String pic;
        private int status;
        private String labels;
        private int add_time;
        private int is_recommend;
        private int main_recommend;
        private String type;
        private int is_freelimit;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public int getView_num() {
            return view_num;
        }

        public void setView_num(int view_num) {
            this.view_num = view_num;
        }

        public int getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(int collect_num) {
            this.collect_num = collect_num;
        }

        public int getWord_num() {
            return word_num;
        }

        public void setWord_num(int word_num) {
            this.word_num = word_num;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLabels() {
            return labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public int getMain_recommend() {
            return main_recommend;
        }

        public void setMain_recommend(int main_recommend) {
            this.main_recommend = main_recommend;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIs_freelimit() {
            return is_freelimit;
        }

        public void setIs_freelimit(int is_freelimit) {
            this.is_freelimit = is_freelimit;
        }
    }
}
