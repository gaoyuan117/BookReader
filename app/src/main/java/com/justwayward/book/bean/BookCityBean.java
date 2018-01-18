package com.justwayward.book.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/17.
 */

public class BookCityBean implements MultiItemEntity {


    private String type;
    private int item_type;
    private List<ListBean> list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    @Override
    public int getItemType() {
        return item_type;
    }

    public static class ListBean {
        /**
         * id : 1
         * title : 金枝夙孽
         * author : 籽日
         * category_id : 1
         * view_num : 0
         * collect_num : 0
         * word_num : 0
         * desc : &nbsp;&nbsp;&nbsp;&nbsp;他说，“我已是王爷，我能给你想要的一切。”&nbsp;&nbsp;&nbsp;&nbsp;她答，“不要喜欢我，我不只是文无忧。&nbsp;&nbsp;&nbsp;&nbsp;我也不会喜欢你，我不曾有那种情愫。”&nbsp;&nbsp;&nbsp;&nbsp;她是万千离弦的箭，穿过层台漫月，烟笼寒水，只向仇人的心。&nbsp;&nbsp;&nbsp;&nbsp;她是万滴轻柔的雨，千娇百媚，回望倾城，只向助她功成之人、奉献一切。&nbsp;&nbsp;&nbsp
         * pic : http://oz4nnu8m9.bkt.clouddn.com/FpYkJoQgbVnAnY0zN01ZayywnWgs
         * status : 0
         * labels :
         * add_time : 1511256087
         * is_recommend : 0
         * main_recommend : 0
         * is_changdu : 0
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
        private int is_changdu;
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

        public int getIs_changdu() {
            return is_changdu;
        }

        public void setIs_changdu(int is_changdu) {
            this.is_changdu = is_changdu;
        }

        public int getIs_freelimit() {
            return is_freelimit;
        }

        public void setIs_freelimit(int is_freelimit) {
            this.is_freelimit = is_freelimit;
        }
    }
}
