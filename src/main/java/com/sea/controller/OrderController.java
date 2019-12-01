package com.sea.controller;

import com.sea.bean.GoodOrder;
import com.sea.bean.User;
import com.sea.dao.OrderMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Result<Object> createOrder(@Validated @RequestBody GoodOrder order,
                                      BindingResult ValidateRs){
        //非空校验
        if(ValidateRs.hasErrors()){
            List<String> errors = new ArrayList<>();
            List<ObjectError> allErrors = ValidateRs.getAllErrors();
            for (ObjectError error: allErrors){
                errors.add(error.getDefaultMessage());
            }
            return ResultUtil.error(ResultEnum.ERROR,errors);
        }
        //后端生成下单时间，订单号
        String ordernum = DateUtil.getyyyyMMddHHmmssSSS();
        String ordetime = DateUtil.getyyyyMMddHHmmss();
        order.setOrdernum(ordernum);
        order.setOrdertime(ordetime);
        int inserRs = orderMapper.insert(order);
        if(inserRs > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }

    }

    @ResponseBody
    @RequestMapping("/myOrder")
    public Result<List<Object>> myOrder(@RequestParam int id){
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buyerid",id);
        List<GoodOrder> list = orderMapper.selectByExample(example);
        if(list.isEmpty()){
            return ResultUtil.success(ResultEnum.SUCCESS,"您还没有订单哦");
        }else {
            return ResultUtil.success(ResultEnum.SUCCESS,list);
        }
    }

    @ResponseBody
    @RequestMapping("/changeOrderStatus")
    public Result<Integer> changeOrderStatus(@RequestParam int orderid, @RequestParam String status){
        //改变订单状态
        GoodOrder order = new GoodOrder();
        order.setOrderstatus(status);
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderid);
        int result = orderMapper.updateByExampleSelective(order,example);
        order = orderMapper.selectByPrimaryKey(orderid);

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }


    @ResponseBody
    @RequestMapping("/changeOrderAddr")
    public Result<Object> changeOrderAddr(@RequestParam int orderid, @RequestParam String address){
        //改变订单地址
        GoodOrder order = new GoodOrder();
        order.setAddress(address);
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderid);
        int result = orderMapper.updateByExampleSelective(order,example);
        order = orderMapper.selectByPrimaryKey(orderid);

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

    @ResponseBody
    @RequestMapping("/changeOrderTel")
    public Result<Object> changeOrderTel(@RequestParam int orderid, @RequestParam String tel){
        //改变订单电话
        GoodOrder order = new GoodOrder();
        order.setTel(tel);
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderid);
        int result = orderMapper.updateByExampleSelective(order,example);
        order = orderMapper.selectByPrimaryKey(orderid);

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

    @ResponseBody
    @RequestMapping("/changeOrderFreghtcharge")
    public Result<Object> changeOrderFreghtcharge(@RequestParam int orderid, @RequestParam BigDecimal freghtcharge){
        //改变订单运费
        GoodOrder order = new GoodOrder();
        order.setFreghtcharge(freghtcharge);
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderid);
        int result = orderMapper.updateByExampleSelective(order,example);
        order = orderMapper.selectByPrimaryKey(orderid);

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

    @ResponseBody
    @RequestMapping("/changeOrderSchool")
    public Result<Object> changeOrderSchool(@RequestParam int orderid, @RequestParam String school){
        //改变订单学校
        GoodOrder order = new GoodOrder();
        order.setSchool(school);
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderid);
        int result = orderMapper.updateByExampleSelective(order,example);
        order = orderMapper.selectByPrimaryKey(orderid);

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

// order+nopay
// paid+nosend
// send+norecv
// received
// tradeok
// cancel
    //结算，order+nopay ---> paid+nosend
    @ResponseBody
    @RequestMapping(value = "/settleOrder", method = RequestMethod.POST)
    public Result<Object> settle(@RequestHeader(value = "Authorization")String token,int orderid, String status, String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status == "order+nopay"){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("paid+nosend");
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("buyerid",dbUser.getId());

            int result = orderMapper.updateByExampleSelective(order,example);
            if(result > 0){
                order = orderMapper.selectByPrimaryKey(orderid);
                return ResultUtil.success(ResultEnum.SUCCESS,order);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }else{
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }


    // order+nopay
// paid+nosend
// send+norecv
// received
// tradeok
// cancel
    //收货，send+norecv ---> received
    @ResponseBody
    @RequestMapping(value = "/receiveOrder", method = RequestMethod.POST)
    public Result<Object> receive(@RequestHeader(value = "Authorization")String token,int orderid, String status, String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status == "send+norecv"){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("received");
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("buyerid",dbUser.getId());

            int result = orderMapper.updateByExampleSelective(order,example);
            order = orderMapper.selectByPrimaryKey(orderid);
            if(result > 0){
                return ResultUtil.success(ResultEnum.SUCCESS,order);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }else{
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

    // order+nopay
// paid+nosend
// send+norecv
// received
// tradeok
// cancel
    //交易成功，received ---> tradeok
    @ResponseBody
    @RequestMapping(value = "/tradeokOrder", method = RequestMethod.POST)
    public Result<Object> tradeok(@RequestHeader(value = "Authorization")String token,int orderid, String status, String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status == "received"){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("tradeok");
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("buyerid",dbUser.getId());

            int result = orderMapper.updateByExampleSelective(order,example);
            order = orderMapper.selectByPrimaryKey(orderid);
            if(result > 0){
                return ResultUtil.success(ResultEnum.SUCCESS,order);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }else{
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }
}
