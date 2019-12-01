package com.sea.controller;

import com.sea.bean.User;
import com.sea.dao.UserMapper;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
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
                        result.put("token", JwtHelper.createToken(username));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result.put("code", 201);
                    result.put("msg", "ok");
                    try {
                        result.put("token", JwtHelper.createToken(username));
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

    @ResponseBody
    @PostMapping("/register")
    public String register(String userName, String password, String school, String answer, String question) {

        if (userName == null) return "userName为null";
        if (password == null) return "password为null";
        if (school == null) return "school为null";
        User dbUser = userMapper.selectByPrimaryKey(userName);
        if (dbUser == null) {
            User user = new User();
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

    @RequestMapping("/index")
    public String index(){
        return "forward:/Goods/index";
    }


}
