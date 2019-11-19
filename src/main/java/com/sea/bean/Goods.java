package com.sea.bean;

import javax.persistence.Column;
import java.math.BigDecimal;

public class Goods {

    private Integer id;

    @Column(name = "userId")
    private Integer userId;

    private String type;

    private String title;

    private String describe;

    private String picture;

    private String school;

    @Column(name = "originalPrice")
    private BigDecimal originalPrice;

    @Column(name = "sellingPrice")
    private BigDecimal sellingPrice;

    @Column(name = "freightCharge")
    private BigDecimal freightCharge;

    private String status;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(BigDecimal freightCharge) {
        this.freightCharge = freightCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", picture='" + picture + '\'' +
                ", school='" + school + '\'' +
                ", originalPrice=" + originalPrice +
                ", sellingPrice=" + sellingPrice +
                ", freightCharge=" + freightCharge +
                ", status='" + status + '\'' +
                '}';
    }
}
