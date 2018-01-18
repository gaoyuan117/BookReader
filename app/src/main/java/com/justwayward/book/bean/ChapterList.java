package com.justwayward.book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gaoyuan on 2017/12/18.
 */

@Entity
public class ChapterList {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "uid")
    @NotNull
    private int uid;

    @Property(nameInDb = "bookId")
    @NotNull
    @Unique
    private String bookId;

    @Property(nameInDb = "js")
    @NotNull
    private String js;

    @Generated(hash = 1978972732)
    public ChapterList(Long id, int uid, @NotNull String bookId,
            @NotNull String js) {
        this.id = id;
        this.uid = uid;
        this.bookId = bookId;
        this.js = js;
    }

    @Generated(hash = 1539606642)
    public ChapterList() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getJs() {
        return this.js;
    }

    public void setJs(String js) {
        this.js = js;
    }

  

}
