package com.sea.controller;

import com.sea.bean.Goods;
import com.sea.dao.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Goods")
public class GoodsController {

    @Autowired
    private GoodsMapper goodsMapper;

    @PostMapping("/releaseGoods")
    @ResponseBody
    public Map<String,String> releaseGoods(String token, String type, String title, String describe, String picture, String school,BigDecimal originalPrice,BigDecimal sellingPrice,BigDecimal freightCharge){
        Goods goods=new Goods();
        Map<String,String> result=new HashMap<>();
        goods.setType(type);
        goods.setTitle(title);
        goods.setDescribe(describe);
        goods.setPicture(picture);
        goods.setSchool(school);
        //goods.setSellingPrice(sellingPrice);
       // goods.setOriginalPrice(originalPrice);
        BigDecimal s=new BigDecimal("5555");
        BigDecimal o=new BigDecimal("5500");
        BigDecimal f=new BigDecimal("20");
        goods.setUserId(10);
        goods.setStatus("在售");
        //goods.setFreightCharge(freightCharge);
        goods.setOriginalPrice(o);
        goods.setSellingPrice(s);
        goods.setFreightCharge(f);
        int n=goodsMapper.insert(goods);
        if (n>0){
            result.put("msg","ok");
        }else {
            result.put("msg","error");
        }
        return result;
    }



}
