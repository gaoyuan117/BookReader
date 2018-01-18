package com.justwayward.book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gaoyuan on 2017/12/25.
 */

@Entity
public class BookSourceBean {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "bookId")
    @NotNull
    private String bookId;

    @Property(nameInDb = "novelId")
    @NotNull
    private String novelId;

    @Property(nameInDb = "source")
    @NotNull
    private String source;

    @Generated(hash = 168822629)
    public BookSourceBean(Long id, @NotNull String bookId, @NotNull String novelId,
            @NotNull String source) {
        this.id = id;
        this.bookId = bookId;
        this.novelId = novelId;
        this.source = source;
    }

    @Generated(hash = 1512565980)
    public BookSourceBean() {
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

    public String getNovelId() {
        return this.novelId;
    }

    public void setNovelId(String novelId) {
        this.novelId = novelId;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
