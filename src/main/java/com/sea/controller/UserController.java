package com.sea.controller;

import com.sea.bean.User;
import com.sea.dao.UserMapper;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    //用户登录
    @ResponseBody
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestParam(name = "userName") String username, @RequestParam(name = "password") String pwd) {
        User dbUser = userMapper.selectByPrimaryKey(username);
        Map<String, Object> result = new HashMap<>();
        if (dbUser != null) {
            if (dbUser.getUserName().equals(username) && dbUser.getPassword().equals(pwd)) {
                if (dbUser.getType().equals("admin")) {
                    result.put("code", 200);
                    result.put("msg", "ok");
                    try {
                        result.put("token", JwtHelper.createToken(username,dbUser.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result.put("code", 201);
                    result.put("msg", "ok");
                    try {
                        result.put("token", JwtHelper.createToken(username,dbUser.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                result.put("code", 500);
                result.put("msg", "error");
            }
        } else {
            result.put("msg", "error");
        }
        return result;
    }

    //用户注册
    @ResponseBody
    @PostMapping("/register")
    public String register(String userName, String password, String school, String answer, String question) {

        if (userName == null) return "userName为null";
        if (password == null) return "password为null";
        if (school == null) return "school为null";
        User dbUser = userMapper.selectByPrimaryKey(userName);
        if (dbUser == null) {
            User user = new User();
            user.setHeadPortrait("http://www.summerstudy.top/2019/12/03/3e5565e142591.jpg");
            user.setUserName(userName);
            user.setNewUser("是");
            user.setPassword(password);
            //user.setPassword(Utils.getEncode(password));
            user.setSchool(school);
            user.setType("normal");
            user.setQuestion(question);
            user.setAnswer(answer);
            int i = userMapper.insert(user);
            if (i > 0) return "注册成功";
        }
        return "用户名已注册";
    }

    //忘记密码
    @ResponseBody
    @PostMapping("/forgetPassword")
    public Map<String, Object> forget(String userName) {
        User dbUser = userMapper.selectByPrimaryKey(userName);
        Map<String, Object> result = new HashMap<>();
        if (dbUser != null) {
            result.put("msg","ok");
            result.put("question", dbUser.getQuestion());
        } else {
            result.put("msg", "error");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/getQuestion")
    public Map<String, String> getQuestion(@RequestHeader(value = "Authorization")String token) {
        String userName=null;
        System.out.println(token);
        if (token!=null){
            userName = JwtHelper.getUserName(token);
        }
        Map<String, String> map = new HashMap<>();
        if (userName != null) {
            User dbUser = userMapper.selectByPrimaryKey(userName);
            if (dbUser != null) {
                map.put("question", dbUser.getQuestion());
                map.put("msg", "ok");
            } else {
                map.put("msg", "error");
            }
        } else {
            map.put("msg", "error,illegal token");
        }
        return map;
    }

    //检查答案
    @ResponseBody
    @PostMapping("/checkAnswer")
    public Map<String, Object> checkAnswer(String answer, HttpSession session,@RequestHeader(value = "Authorization",required = false) String token, String userName) {
        if (userName == null && token != null) {
            userName = JwtHelper.getUserName(token);
        }
        Map<String, Object> result = new HashMap<>();
        if (userName != null) {
            User dbUser = userMapper.selectByPrimaryKey(userName);
            if (dbUser != null && userName.equals(dbUser.getUserName())) {
                if (answer.equals(dbUser.getAnswer())) {
                    session.setAttribute(userName, dbUser);
                    //session有效时间是五分钟
                    session.setMaxInactiveInterval(5*60);
                    result.put("msg", "ok");
                } else {
                    result.put("msg", "error");
                }
            }
            return result;
        } else {
            result.put("msg", "error");
        }
        return result;
    }

    //修改密码
    @ResponseBody
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(String password,@RequestHeader(value = "Authorization",required = false) String token, HttpSession session,String userName) {
        if (token!=null&&userName==null){
            userName=JwtHelper.getUserName(token);
        }
        User sessionUser = (User) session.getAttribute(userName);
        Map<String, Object> result = new HashMap<>();
        if (sessionUser != null) {
            sessionUser.setPassword(password);
            int n = userMapper.updateByPrimaryKey(sessionUser);
            if (n > 0) {
                result.put("msg", "ok");
                session.removeAttribute(userName);
                return result;
            }
        }else {
            result.put("msg", "error timeout");
        }

        return result;
    }

    //修改电话号码
    @PostMapping("/changeTel")
    @ResponseBody
    public Map<String, Object> changeTel(String tel,@RequestHeader(value = "Authorization")String token) {
        User user = new User();
        String userName=JwtHelper.getUserName(token);
        user.setTel(tel);
        user.setUserName(userName);
        int n = userMapper.updateUserTel(user);
        Map<String, Object> result = new HashMap<>();
        if (n > 0) {
            result.put("msg", "ok");
        } else {
            result.put("msg", "error");
        }
        return result;
    }

    //更新用户信息
    @PostMapping("/updateUser")
    @ResponseBody
    public Map<String,Object> updateUser(@RequestHeader(value = "Authorization") String token, String school,
                                         String question, String answer, String sex){
        Map<String,Object> result=new HashMap<>();
        String userName=JwtHelper.getUserName(token);
        if (userName==null){
            result.put("msg","error");
            result.put("error","illegal token");
            return result;
        }
        User user=userMapper.selectByPrimaryKey(userName);
//        if(password!=null&&!user.getPassword().equals(password)){
//            user.setPassword(password);
//        }
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

    //用户中心
    @ResponseBody
    @RequestMapping("/personal")
    public String personal(@RequestHeader(value = "Authorization")String token) throws JSONException {
        String userName=JwtHelper.getUserName(token);
        JSONObject result=new JSONObject();
        if (userName!=null){
            User dbUser=userMapper.selectByPrimaryKey(userName);
            //result.put("password",dbUser.getPassword());
            result.put("school",dbUser.getSchool());
            result.put("answer",dbUser.getAnswer());
            result.put("headPortrait",dbUser.getHeadPortrait());
            result.put("tel",dbUser.getTel());
            result.put("question",dbUser.getQuestion());
            result.put("sex",dbUser.getSex());
            result.put("msg","ok");
            return result.toString();
        }
        result.put("msg","error");
        result.put("error","illegal token");

        return result.toString();
    }

    @RequestMapping("/index")
    public String index(){
        return "forward:/Goods/index";
    }


}
