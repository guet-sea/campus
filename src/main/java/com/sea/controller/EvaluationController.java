package com.sea.controller;

import com.sea.bean.Evaluation;
import com.sea.bean.User;
import com.sea.dao.EvaluationMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.JwtHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping("/giveEvaluation")
    public String giveEvaluation(@RequestHeader(value = "Authorization")String token,Integer goodsId,Integer sellerId,Integer buyerId,String content) throws JSONException {
        String userName= JwtHelper.getUserName(token);
        JSONObject result=new JSONObject();
        if (userName==null){
            result.put("msg","illegal token");
            return result.toString();
        }
        Evaluation evaluation=new Evaluation();
        if (sellerId!=null&&buyerId!=null&&content!=null&&goodsId!=null){
            User buyer=userMapper.queryUserById(buyerId);
            User seller=userMapper.queryUserById(sellerId);
            evaluation.setGoodsId(goodsId);
            evaluation.setUserName(userName);
            evaluation.setBuyerUserName(buyer.getUserName());
            evaluation.setSellerUserName(seller.getUserName());
            evaluation.setContent(content);
            result.put("msg","ok");
        }else{
            result.put("msg","param missing");
        }
        return result.toString();
    }


}
