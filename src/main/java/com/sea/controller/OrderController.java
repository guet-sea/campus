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
    @RequestMapping("/myBuyOrder")
    public Result<List<Object>> myBuyOrder(@RequestParam int id){
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
    @RequestMapping("/mySellOrder")
    public Result<List<Object>> mySellOrder(@RequestParam int id){
        Example example = new Example(GoodOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sellerid",id);
        List<GoodOrder> list = orderMapper.selectByExample(example);
        if(list.isEmpty()){
            return ResultUtil.success(ResultEnum.SUCCESS,"您还没有卖出任何一件东西哦");
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
    public Result<Object> settle(@RequestHeader(value = "Authorization")String token,
                                 int orderid,
                                 String status,
                                 String password){
        System.out.println(status);
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,"name null");
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,"dbUser null");
        }else if(status.equals("orderNopay")){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("paidNosend");
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
            return ResultUtil.error(ResultEnum.ERROR,"status not match");
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
    public Result<Object> receive(@RequestHeader(value = "Authorization")String token,
                                  int orderid,
                                  String status,
                                  String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status.equals("sendNorecv")){
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
    public Result<Object> tradeok(@RequestHeader(value = "Authorization")String token,
                                  int orderid,
                                  String status,
                                  String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status.equals("received")){
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



    //
// orderNopay
// paidNosend        //结算，order+nopay ---> paid+nosend
// sendNorecv        //发货，paid+nosend ---> send+norecv       卖家
// received           //收货，send+norecv ---> received
// tradeok            //交易成功，received ---> tradeok
// acancel bcancel abcancel
//发货，卖家
    @ResponseBody
    @RequestMapping(value = "/sendOrder", method = RequestMethod.POST)
    public Result<Object> sendOrder(@RequestHeader(value = "Authorization")String token,
                                  int orderid,
                                  String status,
                                  String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status.equals("paidNosend")){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("sendNorecv");
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("sellerid",dbUser.getId());

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


    @ResponseBody
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public Result<Object> cancelOrder(@RequestHeader(value = "Authorization")String token,
                                    int orderid,
                                    String status){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else {
            GoodOrder order = orderMapper.selectByPrimaryKey(orderid);
            if(order.getBuyerid() == dbUser.getId()){
                //改变订单状态
                if(status.equals("bcancel")){
                    order.setOrderstatus("abcancel");
                }else {
                    order.setOrderstatus("acancel");
                }
            }else if(order.getSellerid() == dbUser.getId()) {
                //改变订单状态
                if(status.equals("acancel")){
                    order.setOrderstatus("abcancel");
                }else {
                    order.setOrderstatus("bcancel");
                }
            }else{
                return ResultUtil.error(ResultEnum.ERROR);
            }

            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);

            int result = orderMapper.updateByExampleSelective(order,example);
            order = orderMapper.selectByPrimaryKey(orderid);
            if(result > 0){
                return ResultUtil.success(ResultEnum.SUCCESS,order);
            }else {
                return ResultUtil.error(ResultEnum.ERROR,order);
            }
        }
    }
}
