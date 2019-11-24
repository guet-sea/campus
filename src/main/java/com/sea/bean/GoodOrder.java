package com.sea.bean;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Table(name = "goodorder")
public class GoodOrder {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @NotNull(message = "{goodorder.sellerid.notnull}")
    @Column(name = "sellerId")
    private Integer sellerid;

    @NotNull(message = "{goodorder.goodsid.notnull}")
    @Column(name = "goodsId")
    private Integer goodsid;

    @NotNull(message = "{goodorder.buyerid.notnull}")
    @Column(name = "buyerId")
    private Integer buyerid;

    @NotBlank(message = "{goodorder.address.NotBlank}")
    @NotNull(message = "{goodorder.address.notnull}")
    private String address;

    @NotBlank(message = "{goodorder.tel.NotBlank}")
    @NotNull(message = "{goodorder.tel.notnull}")
    private String tel;


    @NotNull(message = "{goodorder.price.notnull}")
    private BigDecimal price;


    @Column(name = "freghtCharge")
    private BigDecimal freghtcharge;

    private BigDecimal discounts;



    @NotNull(message = "{goodorder.finalprice.notnull}")
    @Column(name = "finalPrice")
    private BigDecimal finalprice;



    @Column(name = "orderNum")
    private String ordernum;

    @Column(name = "orderTime")
    private String ordertime;


    @NotBlank(message = "{goodorder.school.NotBlank}")
    @NotNull(message = "{goodorder.school.notnull}")
    private String school;

    @NotBlank(message = "{goodorder.orderstatus.NotBlank}")
    @NotNull(message = "{goodorder.orderstatus.notnull}")
    @Column(name = "orderStatus")
    private String orderstatus;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return sellerId
     */
    public Integer getSellerid() {
        return sellerid;
    }

    /**
     * @param sellerid
     */
    public void setSellerid(Integer sellerid) {
        this.sellerid = sellerid;
    }

    /**
     * @return goodsId
     */
    public Integer getGoodsid() {
        return goodsid;
    }

    /**
     * @param goodsid
     */
    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    /**
     * @return buyerId
     */
    public Integer getBuyerid() {
        return buyerid;
    }

    /**
     * @param buyerid
     */
    public void setBuyerid(Integer buyerid) {
        this.buyerid = buyerid;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return freghtCharge
     */
    public BigDecimal getFreghtcharge() {
        return freghtcharge;
    }

    /**
     * @param freghtcharge
     */
    public void setFreghtcharge(BigDecimal freghtcharge) {
        this.freghtcharge = freghtcharge;
    }

    /**
     * @return discounts
     */
    public BigDecimal getDiscounts() {
        return discounts;
    }

    /**
     * @param discounts
     */
    public void setDiscounts(BigDecimal discounts) {
        this.discounts = discounts;
    }

    /**
     * @return finalPrice
     */
    public BigDecimal getFinalprice() {
        return finalprice;
    }

    /**
     * @param finalprice
     */
    public void setFinalprice(BigDecimal finalprice) {
        this.finalprice = finalprice;
    }

    /**
     * @return orderNum
     */
    public String getOrdernum() {
        return ordernum;
    }

    /**
     * @param ordernum
     */
    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    /**
     * @return time
     */
    public String getOrdertime() {
        return ordertime;
    }

    /**
     * @param time
     */
    public void setOrdertime(String time) {
        this.ordertime = time;
    }

    /**
     * @return school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * @return status
     */
    public String getOrderstatus() {
        return orderstatus;
    }

    /**
     * @param status
     */
    public void setOrderstatus(String status) {
        this.orderstatus = status;
    }

    @Override
    public String toString() {
        return "GoodOrder{" +
                "id=" + id +
                ", sellerid=" + sellerid +
                ", goodsid=" + goodsid +
                ", buyerid=" + buyerid +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", price=" + price +
                ", freghtcharge=" + freghtcharge +
                ", discounts=" + discounts +
                ", finalprice=" + finalprice +
                ", ordernum='" + ordernum + '\'' +
                ", time='" + ordertime + '\'' +
                ", school='" + school + '\'' +
                ", status='" + orderstatus + '\'' +
                '}';
    }
}