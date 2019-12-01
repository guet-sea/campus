package com.sea.controller;

import com.sea.bean.LeaveWord;
import com.sea.bean.User;
import com.sea.dao.LeaveWordMapper;
import com.sea.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/LeaveWord")
public class LeaveWordController {

    @Autowired
    private LeaveWordMapper leaveWordMapper;

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping("/getGoodsLeaveWord")
    public List<LeaveWord> getGoodsLeaveWord(Integer goodsId){
        if (goodsId!=null){
            List<LeaveWord> leaveWords=leaveWordMapper.enquiryGoodsLeaveWord(goodsId);
//            Example example=new Example(LeaveWord.class);
//            Example.Criteria criteria=example.createCriteria();
//            criteria.andEqualTo("goodsId",goodsId);
//            List<LeaveWord> leaveWords=leaveWordMapper.selectByExample(example);
            for (int i=0;i<leaveWords.size();i++){
                String userName=leaveWords.get(i).getUserName();
                User user=userMapper.selectByPrimaryKey(userName);
                leaveWords.get(i).setHeadPortrait(user.getHeadPortrait());
            }
            return leaveWords;
        }
        return null;
    }
}
