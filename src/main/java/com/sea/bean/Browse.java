package com.sea.bean;

import javax.persistence.Column;

public class Browse {


    private Integer id;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "goodsId")
    private Integer goodsId;

    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Browse{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", time='" + time + '\'' +
                '}';
    }
}
