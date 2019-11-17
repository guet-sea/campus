package com.sea.controller;

import com.sea.bean.User;
import com.sea.dao.UserMapper;
import org.apache.ibatis.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping("/getUsers")
    public List<User> getUsers(){
        return userMapper.selectAll();
    }

    @ResponseBody
    @PostMapping("/addUser")
    public Map<String,Object> addUser(String userName,String password,String school,String question,String answer,String sex,String type){
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
            result.put("code",200);
            result.put("msg","注册成功！");
        }else {
            result.put("code",201);
            result.put("msg","用户已注册！");
        }
        return result;
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public Map<String,Object> updateUser(String userName, String password, String school, String question, String answer, String sex){
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
        if (n>0) result.put("msg","更新成功！");
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteUser")
    public Map<String,Object>deleteUser(@RequestParam(name = "userName")String userName){
        System.out.println(userName);
        userMapper.deleteByPrimaryKey(userName);
        Map<String,Object> result=new HashMap<>();
        result.put("msg","删除成功！");
        return result;
    }

    @PostMapping("/getUser")
    @ResponseBody
    public User getUser(@RequestParam(name = "userName")String userName){
        return userMapper.selectByPrimaryKey(userName);
    }


}
