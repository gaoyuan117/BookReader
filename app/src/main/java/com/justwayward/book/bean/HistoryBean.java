package com.justwayward.book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gaoyuan on 2017/11/30.
 */

@Entity
public class HistoryBean {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "bookId")
    @NotNull
    @Unique
    private String bookId;

    @Property(nameInDb = "title")
    private String title;

    @Property(nameInDb = "pic")
    private String pic;

    @Property(nameInDb = "des")
    private String des;

    @Property(nameInDb = "author")
    private String author;

    @Property(nameInDb = "isShelf")
    private boolean isShelf;

    @Generated(hash = 1882242370)
    public HistoryBean(Long id, @NotNull String bookId, String title, String pic,
            String des, String author, boolean isShelf) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.pic = pic;
        this.des = des;
        this.author = author;
        this.isShelf = isShelf;
    }

    @Generated(hash = 48590348)
    public HistoryBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean getIsShelf() {
        return this.isShelf;
    }

    public void setIsShelf(boolean isShelf) {
        this.isShelf = isShelf;
    }
}
