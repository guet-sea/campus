package com.sea.bean;

import javax.persistence.Column;
import java.math.BigDecimal;

public class Order {

    private Integer id;

    @Column(name = "goodsId")
    private Integer goodsId;

    @Column(name = "buyerId")
    private Integer buyerId;

    private String address;

    private  String tel;

    private BigDecimal price;

    @Column(name = "freghtCharge")
    private BigDecimal freghtCharge;

    @Column(name = "discounts")
    private BigDecimal discounts;

    @Column(name = "finalPrice")
    private BigDecimal finalPrice;

    @Column(name = "orderNum")
    private String orderNum;

    private String time;

    private String school;

    private  String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFreghtCharge() {
        return freghtCharge;
    }

    public void setFreghtCharge(BigDecimal freghtCharge) {
        this.freghtCharge = freghtCharge;
    }

    public BigDecimal getDiscounts() {
        return discounts;
    }

    public void setDiscounts(BigDecimal discounts) {
        this.discounts = discounts;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", buyerId=" + buyerId +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", price=" + price +
                ", freghtCharge=" + freghtCharge +
                ", discounts=" + discounts +
                ", finalPrice=" + finalPrice +
                ", orderNum='" + orderNum + '\'' +
                ", time='" + time + '\'' +
                ", school='" + school + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
