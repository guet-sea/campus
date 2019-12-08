package com.sea.controller;

import com.alibaba.fastjson.JSONArray;
import com.sea.bean.Browse;
import com.sea.bean.Goods;
import com.sea.bean.User;
import com.sea.dao.BrowseMapper;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/browse")
public class BrowseController {

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserMapper userMapper;

    //用户浏览记录
    @ResponseBody
    @RequestMapping("/userBrowses")
    public String userBrowses(@RequestHeader(value = "Authorization")String token){
        String userId= JwtHelper.getUserId(token);
        JSONArray result=new JSONArray();
        if (userId==null){
            result.add(Utils.getError());
            result.add(Utils.getIllgalToken());
            return result.toJSONString();
        }
        Example browseExample=new Example(Browse.class);
        Example.Criteria browseCriteria=browseExample.createCriteria();
//        String condition ="userId='"+userId+"'ORDER BY time DESC LIMIT 100";
        browseCriteria.andEqualTo("userId",userId);
        browseExample.setOrderByClause("time desc LIMIT 50");
//        browseCriteria.andCondition(condition);
        List<Browse> browseList=browseMapper.selectByExample(browseExample);
        Iterator<Browse> browseIterator=browseList.iterator();
        List<Goods> goodsList=new ArrayList<>();
        User user=userMapper.queryUserById(Integer.parseInt(userId));
        while (browseIterator.hasNext()){
            int goodsId=browseIterator.next().getGoodsId();
            Goods goods=goodsMapper.selectByPrimaryKey(goodsId);
            goods.setHeadPortrait(user.getHeadPortrait());
            String [] pictures=goods.getPicture().split(",");
            goods.setPicture(pictures[0]);
            goodsList.add(goods);
        }
        result.addAll(goodsList);
        return result.toJSONString();
    }

    //删除浏览记录
    @ResponseBody
    @PostMapping("/deleteBrowse")
    public String deleteBrowse(@RequestHeader(value = "Authorization")String token,Integer goodsId){
        String userId=JwtHelper.getUserId(token);
        JSONArray result=new JSONArray();
        if (userId== null){
            result.add(Utils.getError());
            result.add(Utils.getIllgalToken());
            return result.toJSONString();
        }else if (goodsId==null){
            result.add(Utils.getError());
            result.add(Utils.getParamMiss());
        }else {
            Example example=new Example(Browse.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("userId",userId);
            criteria.andEqualTo("goodsId",goodsId);
            int n=browseMapper.deleteByExample(example);
            if (n>0){
                result.add(Utils.getOk());
            }else {
                result.add(Utils.getError());
                result.add(Utils.getNotCorrespond());
            }
        }
        return result.toJSONString();
    }
    //删除用户浏览记录
    @ResponseBody
    @RequestMapping("/deleteBrowses")
    public String deleteBrowses(@RequestHeader(value = "Authorization")String token){
        String userId=JwtHelper.getUserId(token);
        JSONArray result=new JSONArray();
        if (userId==null){
            result.add(Utils.getError());
            result.add(Utils.getIllgalToken());
            return result.toJSONString();
        }
        Example example=new Example(Browse.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        int n=browseMapper.deleteByExample(example);
        if (n>0){
            result.add(Utils.getOk());
        }else {
            result.add(Utils.getError());
        }
        return result.toJSONString();
    }
}
