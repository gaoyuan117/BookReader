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
public class BookShelfCache {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "uid")
    @NotNull
    private String uid;

    @Property(nameInDb = "js")
    @NotNull
    private String js;

    @Generated(hash = 1587065655)
    public BookShelfCache(Long id, @NotNull String uid, @NotNull String js) {
        this.id = id;
        this.uid = uid;
        this.js = js;
    }

    @Generated(hash = 1546777673)
    public BookShelfCache() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJs() {
        return this.js;
    }

    public void setJs(String js) {
        this.js = js;
    }


}
