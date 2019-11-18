package com.sea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import com.sea.bean.Order;
import com.sea.utils.*;
import com.sea.enums.ResultEnum;

@Controller
public class OrderController {
//    private Order order;

    @ResponseBody
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Result<Order> createOrder(Order order){
    //
        return ResultUtil.success(ResultEnum.SUCCESS,order);
    }

    @ResponseBody
    @RequestMapping("/myOrder")
    public Result<List<Order>> myOrder(@RequestParam int id){
        //查库
        List<Order> list = new ArrayList<Order>();
        Order order = new Order();
        list.add(order);
        list.add(order);
        if(id == 0){
            return ResultUtil.error(ResultEnum.ERROR);
        }else {
            return ResultUtil.success(ResultEnum.SUCCESS,list);
        }
    }

    @ResponseBody
    @RequestMapping("/changeOrderStatus")
    public Result<Integer> changeOrderStatus(@RequestParam int id, @RequestParam int status){
        if(id == 0){
            return ResultUtil.error(ResultEnum.ERROR);
        }else {
            return ResultUtil.success(ResultEnum.SUCCESS,status);
        }
    }
}
