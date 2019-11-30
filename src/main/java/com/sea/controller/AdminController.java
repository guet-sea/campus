package com.sea.controller;

import com.sea.bean.Goods;
import com.sea.bean.User;
import com.sea.dao.GoodsMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    //查询普通用户
    @ResponseBody
    @RequestMapping("/getUsers")
    public List<User> getUsers(@RequestHeader(value = "Authorization",required = false) String token){
        Example example=new Example(User.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("type","normal");
        return userMapper.selectByExample(example);
    }

    @ResponseBody
    @PostMapping("/addUser")
    public Map<String,Object> addUser(@RequestHeader(value = "Authorization",required = false) String token,String userName,String password,String school,String question,String answer,String sex,String type){
        User user=new User();
        Map<String,Object> result=new HashMap<>();
        if (userMapper.selectByPrimaryKey(userName)==null){
            user.setUserName(userName);
            user.setPassword(password);
            user.setSchool(school);
            user.setQuestion(question);
            user.setAnswer(answer);
            user.setSex(sex);
            user.setType(type);
            user.setNewUser("是");
            userMapper.insert(user);
            //result.put("code",200);
            result.put("msg","ok");
        }else {
            //result.put("code",201);
            result.put("msg","error userName exist");
        }
        return result;
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public Map<String,Object> updateUser(@RequestHeader(value = "Authorization",required = false) String token,String userName, String password, String school, String question, String answer, String sex){
        Map<String,Object> result=new HashMap<>();
        User user=userMapper.selectByPrimaryKey(userName);
        if(password!=null&&!user.getPassword().equals(password)){
            user.setPassword(password);
        }
        if (school!=null&&!user.getSchool().equals(school)){
            user.setSchool(school);
        }
        if (question!=null&&!user.getQuestion().equals(question)){
            user.setQuestion(question);
        }
        if (answer!=null&&!user.getAnswer().equals(answer)){
            user.setAnswer(answer);
        }
        if (sex!=null&&!user.getSex().equals(sex)){
            user.setSex(sex);
        }
        System.out.println(user);
        int n= userMapper.updateByPrimaryKey(user);
        if (n>0) result.put("msg","ok");
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteUser")
    public Map<String,Object>deleteUser(@RequestParam(name = "userName")String userName){
        System.out.println(userName);
        userMapper.deleteByPrimaryKey(userName);
        Map<String,Object> result=new HashMap<>();
        result.put("msg","ok");
        return result;
    }

    @PostMapping("/getUser")
    @ResponseBody
    public Map<String,String> getUser(@RequestParam(name = "userName")String userName){
        Map<String,String> map=new HashMap<>();
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            map.put("msg","ok");
        }else {
            map.put("msg","error");
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/addAdmin")
    public Map<String,String> addAdmin(String userName,String password){
        User user=new User();
        User dbUser=userMapper.selectByPrimaryKey(userName);
        Map<String,String> result=new HashMap<>();
        if (dbUser==null){
            user.setUserName(userName);
            user.setType("admin");
            user.setPassword(password);
            int n= userMapper.insert(user);
            if (n>0){
                result.put("msg","ok");
                return result;
            }else{
                result.put("msg","error");
                return result;
            }
        }else {
            result.put("msg","userName exist");
        }
        return  result;
    }

    @ResponseBody
    @RequestMapping("/getAllGoods")
    public List<Goods> getAllGoods(){
        return goodsMapper.selectAll();
    }

    @ResponseBody
    @RequestMapping("/getGoodsOnSell")
    public List<Goods> getGoodsOnSell(){
        Example example=new Example(Goods.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("status","在售");
        return goodsMapper.selectByExample(example);
    }

    @ResponseBody
    @RequestMapping("/deleteGoods")
    public String deleteGoods(Integer id) throws JSONException {
        int n=goodsMapper.deleteByPrimaryKey(id);
        JSONObject result=new JSONObject();
        if (n>0)result.put("msg","ok");
        else result.put("msg","error");
        return result.toString();
    }

    @RequestMapping("/updateGoods")
    public String updateGoods(){
        return "forward:/Goods/updateGoods";
    }

    @RequestMapping("/addGoods")
    public String addGoods(){
        return "forWard:/Goods/releaseGoods";
    }

    @RequestMapping("/getGoods")
    public String getGoods(){
        return "forward:/Goods/getGoods";
    }

    @RequestMapping("/searchGoodsOnSell")
    public String searchGoodsOnSell(){
        return "forward:/Goods/searchLike";
    }

    @ResponseBody
    @PostMapping("/searchGoods")
    public List<Goods> searchGoods(String key){
        Example example=new Example(Goods.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andLike("title", Utils.getLike(key));
        criteria.orLike("describe",Utils.getLike(key));
        List<Goods> list=goodsMapper.selectByExample(example);
        for (int i=0;i<list.size();i++){
            int id=list.get(i).getUserId();
            list.get(i).setHeadPortrait(userMapper.queryUserById(id).getHeadPortrait());
        }
        return list;
    }




}
