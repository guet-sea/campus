package com.sea.controller;

import com.sea.bean.GoodOrder;
import com.sea.dao.OrderMapper;
import com.sea.utils.DateUtil;
import com.sea.utils.Result;
import com.sea.utils.ResultEnum;
import com.sea.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

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
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    public Result<Object> updateOrder(@Validated @RequestBody GoodOrder order,
                                           BindingResult Validate){
        //非空校验
        if(Validate.hasErrors()){
            List<String> errors = new ArrayList<>();
            List<ObjectError> allErrors = Validate.getAllErrors();
            for (ObjectError error: allErrors){
                errors.add(error.getDefaultMessage());
            }
            return ResultUtil.error(ResultEnum.ERROR,errors);
        }
        int updateRs = orderMapper.updateByPrimaryKey(order);
        if(updateRs > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,order);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }
}
