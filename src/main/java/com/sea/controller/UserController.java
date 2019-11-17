package com.sea.controller;

import com.sea.bean.User;
import com.sea.dao.UserMapper;
import com.sea.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping ("/login")
    public Map<String,Object> login(@RequestParam(name = "username")String username, @RequestParam(name = "password")String pwd, Model model){
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

}
