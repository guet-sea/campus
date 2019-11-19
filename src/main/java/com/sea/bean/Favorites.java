package com.sea.bean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "collection")
public class Favorites {

    private  Integer id;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "goodsId")
    private Integer goodsId;

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

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                '}';
    }
}
