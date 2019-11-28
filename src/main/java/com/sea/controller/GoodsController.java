package com.sea.controller;

import com.sea.bean.Goods;
import com.sea.bean.User;
import com.sea.dao.GoodsMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/Goods")
public class GoodsController {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/releaseGoods")
    @ResponseBody
    public Map<String,String> releaseGoods(@RequestHeader(value = "Authorization")String token, String type, String title, String describe, String picture, String school, BigDecimal originalPrice, BigDecimal sellingPrice, BigDecimal freightCharge){
        Goods goods=new Goods();
        Map<String,String> result=new HashMap<>();
        String userName=JwtHelper.getUserName(token);
        if (userName==null){
            result.put("msg","error,illegal token");
            return result;
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            result.put("msg","userName not exist");
            return result;
        }
        //List <String> pictureList=new ArrayList<>();
        goods.setType(type);
        goods.setTitle(title);
        goods.setDescribe(describe);
        //pictureList.add(picture);
        goods.setPicture(picture);
        goods.setSchool(school);
        goods.setSellingPrice(sellingPrice);
        goods.setOriginalPrice(originalPrice);
        goods.setUserId(dbUser.getId());
        goods.setStatus("在售");
        goods.setFreightCharge(freightCharge);
        int n=goodsMapper.insert(goods);
        if (n>0){
            result.put("msg","ok");
        }else {
            result.put("msg","error");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/searchLike")
    public List<Goods> searchByLike(String key){
        List<Goods> list=goodsMapper.searchLikeByTitleOrDescribe(key);
        List<Goods> goodsList=new ArrayList<>();
        Iterator<Goods> iterator=list.iterator();
        while (iterator.hasNext()){
            goodsList.add(iterator.next());
            String [] strings=goodsList.get(goodsList.size()-1).getPicture().get(0).split(",");
            goodsList.get(goodsList.size()-1).setPicture(strings[0]);
         }
        return list;
    }

    @PostMapping("/updateGoods")
    @ResponseBody
    public Map<String,String> updateGoods(Integer id,String type, String title, String describe, String picture, String school,BigDecimal originalPrice,BigDecimal sellingPrice,BigDecimal freightCharge){
        Goods goods=goodsMapper.selectByPrimaryKey(id);
        if (type!=null){
            goods.setType(type);
        }
        if (title!=null)goods.setTitle(title);
        if (describe!=null)goods.setDescribe(describe);
        if (picture!=null)goods.setPicture(picture);
        if (school!=null)goods.setPicture(picture);
        if (originalPrice!=null)goods.setOriginalPrice(originalPrice);
        if (sellingPrice!=null)goods.setSellingPrice(sellingPrice);
        if (freightCharge!=null)goods.setFreightCharge(freightCharge);
        int n=goodsMapper.updateByPrimaryKey(goods);
        HashMap<String,String> map=new HashMap<>();
        if (n>0) {
            map.put("msg","ok");
        } else {
            map.put("msg","error");
        }
        return map;
    }

    @PostMapping("/getGoods")
    @ResponseBody
    public Goods  getGoods(Integer id){
        return goodsMapper.selectByPrimaryKey(id);
    }



}
