package com.sea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sea.bean.Favorites;
import com.sea.bean.Goods;
import com.sea.bean.User;
import com.sea.dao.FavoritesMapper;
import com.sea.dao.GoodsMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.DateUtil;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Controller
@RequestMapping("/favorite")
public class FavoritesController {

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;

    //添加收藏
    @ResponseBody
    @PostMapping("/addFavorites")
    public String addFavorites( @RequestHeader(value = "Authorization")String token,Integer goodsId){
        JSONArray result=new JSONArray();
        String username= JwtHelper.getUserName(token);
        String userId=JwtHelper.getUserId(token);
        if (username==null||userId==null){
            result.add(Utils.getError());
            result.add(Utils.getIllgalToken());
            return result.toJSONString();
        }else if (goodsId!=null){
            Favorites favorites=new Favorites();
            favorites.setGoodsId(goodsId);
            favorites.setUserId(Integer.parseInt(userId));
            favorites.setTime(DateUtil.getyyyyMMddHHmmss());
            int n=favoritesMapper.insert(favorites);
            if (n>0){
                result.add(Utils.getOk());
            }else{
                result.add(Utils.getError());
            }
            return result.toJSONString();
        }
        return null;
    }

    //获取用户收藏
    @RequestMapping("/userFavorites")
    @ResponseBody
    public String userFavorites(@RequestHeader(value = "Authorization")String token){
        String userId=JwtHelper.getUserId(token);
        String userName=JwtHelper.getUserName(token);
        JSONArray result=new JSONArray();
        if (userId==null||userName==null){
            result.add(Utils.getError());
            result.add(Utils.getIllgalToken());
            return result.toJSONString();
        }else {
            Example example=new Example(Favorites.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("userId",userId);
            example.setOrderByClause("time desc");
            List<Favorites> favorites=favoritesMapper.selectByExample(example);
            User user=userMapper.selectByPrimaryKey(userName);
            if (!favorites.isEmpty()){
                List<Goods> goodsList=new ArrayList<>();
                Iterator<Favorites> iterator=favorites.iterator();
                while (iterator.hasNext()){
                    Goods goods=goodsMapper.selectByPrimaryKey(iterator.next().getGoodsId());
                    goods.setHeadPortrait(user.getHeadPortrait());
                    String [] pictures=goods.getPicture().split(",");
                    goods.setPicture(pictures[0]);
                    goodsList.add(goods);
                }
                result.addAll(goodsList);
                return result.toJSONString();
            }
        }
        result.add(Utils.getError());
        return result.toJSONString();
    }

    //取消收藏
    @ResponseBody
    @PostMapping("/deleteFavorite")
    public String deleteFavorite(Integer goodsId,@RequestHeader(value = "Authorization")String token){
        JSONArray result=new JSONArray();
        String userId=JwtHelper.getUserId(token);
        if (userId==null){
            result.add(Utils.getError());
            result.add(Utils.getIllgalToken());
            return result.toJSONString();
        }else if (goodsId!=null){
            Example example=new Example(Favorites.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("userId",userId);
            criteria.andEqualTo("goodsId",goodsId);
            int n=favoritesMapper.deleteByExample(example);
            if (n>0){
                result.add(Utils.getOk());
            }else {
                result.add(Utils.getError());
                result.add(Utils.getNotCorrespond());
//                Map<String,String> map=new HashMap<>();
//                map.put("error","该商品没有被userId："+userId+"收藏");
                //result.add(map);
            }
        }
        return result.toJSONString();
    }

}
