package com.sea.controller;

import com.sea.bean.User;
import com.sea.dao.UserMapper;
import com.sea.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping ("/login")
    public Map<String,Object> login(@RequestParam(name = "userName")String username, @RequestParam(name = "password")String pwd){
        User dbUser= userMapper.selectByPrimaryKey(username);
        Map<String,Object> result=new HashMap<>();
       if(dbUser!=null){
            if (dbUser.getUserName().equals(username)&&dbUser.getPassword().equals(pwd)){
                if (dbUser.getType().equals("admin")){
                    result.put("msg",200);
                    result.put("msg","登录成功！");
                }else {
                    result.put("msg",201);
                    result.put("msg","登录成功！");
                }
            }else{
                result.put("code",500);
                result.put("msg","密码错误!");
            }
        }else{
            result.put("msg","用户不存在！");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/register")
    public String register(String userName,String password,String school,String answer,String question){

        if (userName==null)return "userName为null";
        if (password==null)return "password为null";
        if (school==null)return "school为null";
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            User user =new User();
            user.setUserName(userName);
            user.setNewUser("是");
            user.setPassword(password);
            //user.setPassword(Utils.getEncode(password));
            user.setSchool(school);
            user.setType("normal");
            user.setQuestion(question);
            user.setAnswer(answer);
            int i=userMapper.insert(user);
            if (i>0)return "{msg:注册成功！}";
        }
        return "{msg:用户名已注册！}";
    }

    @ResponseBody
    @PostMapping("/forgetPassword")
    public Map<String,Object> forget(String userName){
        User dbUser=userMapper.selectByPrimaryKey(userName);
        Map<String,Object> result=new HashMap<>();
        if (dbUser!=null){
            result.put("密保问题",dbUser.getQuestion());
        }else{
            result.put("msg","用户名错误！");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/checkAnswer")
    public Map<String,Object> checkAnswer(String answer, String userName, HttpSession session){
        User dbUser=userMapper.selectByPrimaryKey(userName);
        Map<String,Object> result=new HashMap<>();
        if (dbUser!=null){
            if (answer.equals(dbUser.getAnswer())){
                session.setAttribute(userName,dbUser);
                //session有效时间是三分钟
                session.setMaxInactiveInterval(60*5);
                result.put("msg","ok");
            }else {
                result.put("msg","error");
            }
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/changePassword")
    public Map<String,Object> changePassword(String password,String userName,HttpSession session){
        User sessionUser=(User) session.getAttribute(userName);
        Map<String,Object> result=new HashMap<>();
        if (sessionUser!=null){
            sessionUser.setPassword(password);
            int n= userMapper.updateByPrimaryKey(sessionUser);
            if(n>0){
                result.put("msg","修改密码成功！");
                session.removeAttribute(userName);
                return result;
            }
        }
        result.put("msg","修改密码失败！");
        return result;
    }

    @PostMapping("/changeTel")
    @ResponseBody
    public Map<String,Object> changeTel(String tel,String userName){
        User user=new User();
        user.setTel(tel);
        user.setUserName(userName);
        int n=userMapper.updateUserTel(user);
        Map<String,Object> result=new HashMap<>();
        if (n>0){
            result.put("msg","电话修改成功！");
        }else {
            result.put("msg","电话修改失败！");
        }
        return result;
    }



}
