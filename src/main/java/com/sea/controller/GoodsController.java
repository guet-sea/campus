package com.sea.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sea.bean.Browse;
import com.sea.bean.Favorites;
import com.sea.bean.Goods;
import com.sea.bean.User;
import com.sea.dao.BrowseMapper;
import com.sea.dao.FavoritesMapper;
import com.sea.dao.GoodsMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.DateUtil;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
import org.json.JSONException;
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

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Autowired
    private BrowseMapper browseMapper;

    private List<Goods> getGoodsUserHeadPortrait(List<Goods> goodsList){
        for (int i=0;i<goodsList.size();i++){
            Goods goods=goodsList.get(i);
            goods.setHeadPortrait(userMapper.queryUserById(goods.getUserId()).getHeadPortrait());
            String [] picture=goodsList.get(i).getPicture().split(",");
            goodsList.get(i).setPicture(picture[0]);
        }
        return goodsList;
    }

    //发布商品
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
        if (type==null||title==null||describe==null||picture==null||school==null||sellingPrice==null||originalPrice==null||freightCharge==null){
            result.put("msg","param missing");
            return result;
        }
        List <String> pictureList=new ArrayList<>();
        goods.setType(type);
        goods.setTitle(title);
        goods.setDescribe(describe);
        pictureList.add(picture);
        goods.setPicture(picture);
        goods.setSchool(school);
        goods.setSellingPrice(sellingPrice);
        goods.setOriginalPrice(originalPrice);
        goods.setUserId(dbUser.getId());
        goods.setStatus("在售");
        goods.setFreightCharge(freightCharge);
        goods.setHeadPortrait(dbUser.getHeadPortrait());
        int n=goodsMapper.insert(goods);
        if (n>0){
            result.put("msg","ok");
        }else {
            result.put("msg","error");
        }
        return result;
    }

    //搜索
    @ResponseBody
    @PostMapping("/searchLike")
    public List<Goods> searchByLike(String key){
        List<Goods> list=goodsMapper.searchLikeByTitleOrDescribe(key);
        List<Goods> goodsList=new ArrayList<>();
        Iterator<Goods> iterator=list.iterator();
        while (iterator.hasNext()){
            goodsList.add(iterator.next());
            String [] strings=goodsList.get(goodsList.size()-1).getPicture().split(",");
            goodsList.get(goodsList.size()-1).setPicture(strings[0]);
            Goods goods=goodsList.get(goodsList.size()-1);
            Example example=new Example(User.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("id",goods.getUserId());
            goodsList.get(goodsList.size()-1).setHeadPortrait(userMapper.selectOneByExample(example).getHeadPortrait());
         }
        return list;
    }

    //修改商品信息
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
        if (school!=null)goods.setSchool(school);
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

    //获取商品详情
    @PostMapping("/getGoods")
    @ResponseBody
    public String getGoods(@RequestHeader(value = "Authorization")String token,Integer id){
        String userId=JwtHelper.getUserId(token);
        JSONArray result=new JSONArray();
        if (id==null||userId==null){
            result.add(Utils.getError());
            return result.toJSONString();
        }

        Goods goods=goodsMapper.selectByPrimaryKey(id);
        if (goods==null){
            result.add(Utils.getError());
            return result.toJSONString();
        }
        User user=userMapper.queryUserById(goods.getUserId());
        if (user==null){
            result.add(Utils.getError());
            return result.toJSONString();
        }
        Example example=new Example(Favorites.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",user.getId());
        criteria.andEqualTo("goodsId",goods.getId());
        Favorites favorites=favoritesMapper.selectOneByExample(example);
        if (favorites==null){
            goods.setFavorite("no");
        }else{
            goods.setFavorite("yes");
        }
        goods.setHeadPortrait(user.getHeadPortrait());
        goods.setUserName(user.getUserName());
        String [] pictures=goods.getPicture().split(",");
        goods.setPictures(Arrays.asList(pictures));
        result.add(goods);
        Browse browse=new Browse();
        browse.setUserId(Integer.parseInt(userId));
        browse.setGoodsId(id);
        browse.setTime(DateUtil.getyyyyMMddHHmmss());
        browseMapper.insert(browse);
        return result.toJSONString();
    }

    //首页
    @ResponseBody
    @RequestMapping("/index")
    public List<Goods> index(){
        List<Goods> goodsList=goodsMapper.enquiryForLatestTen();
        Iterator<Goods> iterator=goodsList.iterator();
        while (iterator.hasNext()){
            Goods goods= iterator.next();
            int n=goodsList.indexOf(goods);
            Example example=new Example(User.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("id",goods.getUserId());
            goods.setHeadPortrait(userMapper.selectOneByExample(example).getHeadPortrait());
            String [] picture=goodsList.get(n).getPicture().split(",");
            goodsList.get(n).setPicture(picture[0]);
        }
        return goodsList;
    }

    //获取当前校区的最新十个商品
    @ResponseBody
    @PostMapping("/getSchoolGoods")
    public List<Goods> getSchoolGoods(String school) throws JSONException {
        if (school==null){
            return null;
        }
        List<Goods> goodsList=goodsMapper.queryGoodsOnSchoolForLatestTen(school);
        goodsList=getGoodsUserHeadPortrait(goodsList);
        return goodsList;
    }


    //获取所有商品
    @ResponseBody
    @RequestMapping("/getAllGoods")
    public List<Goods> getAllGoods(){
        return getGoodsUserHeadPortrait(goodsMapper.selectAll());
    }

    //获取该校区所有商品
    @ResponseBody
    @PostMapping("/getSchoolAllGoods")
    public List<Goods> getSchoolAllGoods(String school){
        if (school==null)return null;
        Example example=new Example(Goods.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("status","在售");
        criteria.andEqualTo("school",school);
        return getGoodsUserHeadPortrait(goodsMapper.selectByExample(example));
    }

}
