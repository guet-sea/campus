package com.sea.controller;

import com.sea.bean.GoodOrder;
import com.sea.bean.User;
import com.sea.bean.Goods;
import com.sea.dao.GoodsMapper;
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
    @Autowired
    private GoodsMapper goodsMapper;

    @ResponseBody
    @RequestMapping(value = "/createOrderWithoutPay", method = RequestMethod.POST)
    public Result<Object> createOrderWithoutPay(@Validated @RequestBody GoodOrder order,
                                                @RequestHeader(value = "Authorization")String token,
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

        //后端填写买方id 状态为orderNopay
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else {
            //设置订单状态为orderNopay
            order.setOrderstatus("待付款");
            order.setBuyerid(dbUser.getId());

            int inserRs = orderMapper.insert(order);

            //查出关联的goods
            Goods goods = new Goods();
            goods.setStatus("售出");
            Example example = new Example(Goods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",order.getGoodsid());
            //设置商品状态为售出
            int updateGoodsRs = goodsMapper.updateByExampleSelective(goods,example);
            //设置goods图片
            goods = goodsMapper.selectByPrimaryKey(order.getGoodsid());
            String [] picture=goods.getPicture().split(",");
            order.setGoodsPic(picture[0]);

            if(inserRs > 0  && updateGoodsRs>0){
                return ResultUtil.success(ResultEnum.SUCCESS,order);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/createOrderWithPay", method = RequestMethod.POST)
    public Result<Object> createOrderWithPay(@Validated @RequestBody GoodOrder order,
                                             @RequestHeader(value = "Authorization")String token,
                                             BindingResult ValidateRs,
                                             String password){
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

        //后端填写买方id 状态为paidNosend
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else {
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                System.out.println(dbUser.getPassword());
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，设置订单状态为paidNosend
            order.setOrderstatus("待发货");
            order.setBuyerid(dbUser.getId());

            int inserRs = orderMapper.insert(order);
            //查出关联的goods
            Goods goods = new Goods();
            goods.setStatus("售出");
            Example example = new Example(Goods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",order.getGoodsid());
            //设置商品状态为售出
            int updateGoodsRs = goodsMapper.updateByExampleSelective(goods,example);
            //设置goods图片
            goods = goodsMapper.selectByPrimaryKey(order.getGoodsid());
            String [] picture=goods.getPicture().split(",");
            order.setGoodsPic(picture[0]);

            if(inserRs > 0 && updateGoodsRs>0){
                return ResultUtil.success(ResultEnum.SUCCESS,order);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }
    }


    @ResponseBody
    @RequestMapping("/myBuyOrder")
    public Result<List<Object>> myBuyOrder(@RequestHeader(value = "Authorization")String token){
        String userName= JwtHelper.getUserName(token);
        String id = JwtHelper.getUserId(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }

        List<GoodOrder> list = orderMapper.getMyBuyOrderListWithPic(Integer.parseInt(id));
        if(list.isEmpty()){
            return ResultUtil.success(ResultEnum.SUCCESS,"您还没有订单哦");
        }else {
            String strPic;
            int goodsId;
            Goods goods;
            for(int i=0;i<list.size();i++){
                goodsId = list.get(i).getGoodsid();
                goods = goodsMapper.selectByPrimaryKey(goodsId);
                strPic = goods.getPicture();
                if(strPic != null){
                    String [] picture = strPic.split(",");
                    list.get(i).setGoodsPic(picture[0]);
                }
            }
            return ResultUtil.success(ResultEnum.SUCCESS,list);
        }
    }

    @ResponseBody
    @RequestMapping("/mySellOrder")
    public Result<List<Object>> mySellOrder(@RequestHeader(value = "Authorization")String token){
        String userName= JwtHelper.getUserName(token);
        String id = JwtHelper.getUserId(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }

        List<GoodOrder> list = orderMapper.getMySellOrderListWithPic(Integer.parseInt(id));
        if(list.isEmpty()){
            return ResultUtil.success(ResultEnum.SUCCESS,"您还没有卖出任何一件东西哦");
        }else {
            String strPic;
            int goodsId;
            Goods goods;
            for(int i=0;i<list.size();i++){
                goodsId = list.get(i).getGoodsid();
                goods = goodsMapper.selectByPrimaryKey(goodsId);
                strPic = goods.getPicture();
                if(strPic != null){
                    String [] picture = strPic.split(",");
                    list.get(i).setGoodsPic(picture[0]);
                }
            }
            return ResultUtil.success(ResultEnum.SUCCESS,list);
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

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS);
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

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS);
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

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS);
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

        if(result > 0){
            return ResultUtil.success(ResultEnum.SUCCESS);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

// order+nopay
// paid+nosend
// send+norecv
// received  tradeok
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
        }else if(status.equals("待付款")){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("待发货");
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("buyerid",dbUser.getId());

            int result = orderMapper.updateByExampleSelective(order,example);
            if(result > 0){
                return ResultUtil.success(ResultEnum.SUCCESS);
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
// received  tradeok
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
        }else if(status.equals("待收货")){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("已完成");
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("buyerid",dbUser.getId());

            int result = orderMapper.updateByExampleSelective(order,example);
            if(result > 0){
                return ResultUtil.success(ResultEnum.SUCCESS);
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
// received  tradeok          //收货，send+norecv ---> received 交易成功 tradeok

// acancel bcancel abcancel
//发货，卖家
    @ResponseBody
    @RequestMapping(value = "/sendOrder", method = RequestMethod.POST)
    public Result<Object> sendOrder(@RequestHeader(value = "Authorization")String token,
                                    int orderid,
                                    String status,
                                    String logNum,
                                    String password){
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR,token);
        }else if(status.equals("待发货")){
            //密码错误，直接返回
            if(!dbUser.getPassword().equals(password)){
                return ResultUtil.error(ResultEnum.ERROR_PWDWRONG);
            }
            //密码正确，开始改变订单状态
            GoodOrder order = new GoodOrder();
            order.setOrderstatus("待收货");
            order.setLogNum(logNum);
            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);
            criteria.andEqualTo("sellerid",dbUser.getId());

            int result = orderMapper.updateByExampleSelective(order,example);
            if(result > 0){
                return ResultUtil.success(ResultEnum.SUCCESS);
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
                if(status.equals("卖家取消")){
                    order.setOrderstatus("订单已取消");
                }else {
                    order.setOrderstatus("买家取消");
                }
            }else if(order.getSellerid() == dbUser.getId()) {
                //改变订单状态
                if(status.equals("买家取消")){
                    order.setOrderstatus("订单已取消");
                }else {
                    order.setOrderstatus("卖家取消");
                }
            }else{
                return ResultUtil.error(ResultEnum.ERROR);
            }

            Example example = new Example(GoodOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",orderid);
            criteria.andEqualTo("orderstatus",status);

            int result = orderMapper.updateByExampleSelective(order,example);
            if(result > 0){
                if(order.getOrderstatus().equals("订单已取消")){
                    //查出关联的goods
                    Goods goods = new Goods();
                    goods.setStatus("在售");
                    Example goodsExample = new Example(Goods.class);
                    Example.Criteria goodsCriteria = goodsExample.createCriteria();
                    goodsCriteria.andEqualTo("id",order.getGoodsid());
                    //设置商品状态为在售
                    goodsMapper.updateByExampleSelective(goods,goodsExample);
                }
                return ResultUtil.success(ResultEnum.SUCCESS);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }
    }
}
