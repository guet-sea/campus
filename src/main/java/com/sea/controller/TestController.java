package com.sea.controller;

import com.sea.bean.Test;
import com.sea.bean.User;
import com.sea.dao.TestMapper;
import com.sea.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping("/test")
    public String test(String userName, @RequestParam(name = "url") String url){
        Test test=new Test();
        System.out.println("username；"+userName);
        System.out.println("url:"+url);
        User user=userMapper.selectByPrimaryKey(userName);
        System.out.println(user);
        test.setuId(user.getId());
        test.setUrl(url);
        int i=testMapper.insert(test);
        System.out.println(test);
        if (i>0)return "200";
        return  "405";
    }

}
