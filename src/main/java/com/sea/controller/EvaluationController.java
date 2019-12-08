package com.sea.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sea.bean.Evaluation;
import com.sea.bean.GoodOrder;
import com.sea.bean.User;
import com.sea.dao.EvaluationMapper;
import com.sea.dao.OrderMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.DateUtil;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/Evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderMapper orderMapper;

    //评价
    @ResponseBody
    @PostMapping("/giveEvaluation")
    public String giveEvaluation(@RequestHeader(value = "Authorization")String token,Integer goodsId,
                                                                        String content){
        String userName= JwtHelper.getUserName(token);
        JSONObject result=new JSONObject();
        if (userName==null){
            result.put("msg","illegal token");
            return result.toString();
        }
        Evaluation evaluation=new Evaluation();
        if (content!=null&&goodsId!=null){
            Example example=new Example(GoodOrder.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("goodsid",goodsId);
            criteria.andEqualTo("orderstatus","1");
            GoodOrder goodsOrder=orderMapper.selectOneByExample(example);
            User seller=userMapper.queryUserById(goodsOrder.getSellerid());
            User buyer=userMapper.queryUserById(goodsOrder.getBuyerid());
            evaluation.setBuyerUserName(buyer.getUserName());
            evaluation.setSellerUserName(seller.getUserName());
            evaluation.setGoodsId(goodsId);
            evaluation.setUserName(userName);
            evaluation.setContent(content);
            evaluation.setTime(DateUtil.getyyyyMMddHHmmss());
            evaluationMapper.insert(evaluation);
            result.put("msg","ok");
        }else{
            result.put("error","param missing");
            result.put("msg","error");
        }
        return result.toString();
    }

    //用户收到的评价
    @RequestMapping("/userEvaluation")
    @ResponseBody
    public String getUserEvaluation(@RequestHeader(value = "Authorization")String token){
        String userName=JwtHelper.getUserName(token);
        JSONArray result=new JSONArray();
        if (userName==null){
         result.add(Utils.getError());
         result.add(Utils.getIllgalToken());
         return result.toJSONString();
        }
        Example example=new Example(Evaluation.class);
        Example.Criteria criteria=example.createCriteria();
        String condition="( sellerUserName='"+userName+" 'or buyerUserName='"+userName+"') and userName!='"+userName+"'";
        criteria.andCondition(condition);
        result.addAll(evaluationMapper.selectByExample(example));
        result.add(Utils.getOk());
        return result.toJSONString();
    }




}
