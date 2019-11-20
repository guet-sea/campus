package com.sea.bean;

import javax.persistence.Column;

public class Evaluation {

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "buyerId")
    private Integer buyerId;

    @Column(name = "sellerId")
    private Integer sellerId;

    @Column(name = "goodsId")
    private Integer goodsId;

    private String content;

    private String time;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "userId=" + userId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", goodsId=" + goodsId +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
