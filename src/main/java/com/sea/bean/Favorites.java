package com.sea.bean;

import javax.persistence.Column;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Table(name = "collection")
public class Favorites {

    private  Integer id;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "goodsId")
    private Integer goodsId;

    private Date time;


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
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public void setTime(String time) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.time=sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", time='" + time + '\'' +
                '}';
    }
}
