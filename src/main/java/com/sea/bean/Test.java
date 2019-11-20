package com.sea.bean;

import javax.persistence.Column;
import javax.persistence.Id;

public class Test {

    @Id
    private Integer id;
    @Column(name = "uId")
    private Integer uId;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", uId=" + uId +
                ", url='" + url + '\'' +
                '}';
    }
}
