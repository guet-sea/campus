package com.sea.controller;

import com.alibaba.fastjson.JSONArray;
import com.sea.bean.Address;
import com.sea.bean.Evaluation;
import com.sea.bean.Goods;
import com.sea.dao.AddressMapper;
import com.sea.utils.JwtHelper;
import com.sea.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Controller
@RequestMapping("/Address")
public class AddressController {

    @Autowired
    private AddressMapper addressMapper;

    @ResponseBody
    @RequestMapping("/getDefaultAddress")
    public String getDefaultAddress(@RequestHeader(value = "Authorization") String token) throws JSONException {
        //if (token==null)token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJXRUIiLCJpc3MiOiJTZXJ2aWNlIiwidXNlck5hbWUiOiJyb290IiwiZXhwIjoxNTc1MTk4OTE4LCJpYXQiOjE1NzUxODgxMTh9.f6sU9Qn-oBf-7Y_B-mfRc16yfyknZKcNYIgN4MSBdIs";
        String userName = JwtHelper.getUserName(token);
        JSONObject result = new JSONObject();
        if (userName != null) {
            Address address = addressMapper.queryDefaultAddress(userName);
            if (address==null){
                Example example=new Example(Address.class);
                Example.Criteria criteria=example.createCriteria();
                criteria.andEqualTo("userName",userName);
                address=addressMapper.selectOneByExample(example);
            }
            if (address==null){
                result.put("msg","not address");
                return result.toString();
            }
            result.put("name", address.getFullName());
            result.put("address", address.getAddress());
            result.put("tel", address.getTel());
            result.put("msg","ok");
            return result.toString();
        }
        result.put("msg", "error");
        return result.toString();
    }

    //获取用户的收货地址
    @RequestMapping("/getUserAddress")
    @ResponseBody
    public String getUserAddress(@RequestHeader(value = "Authorization") String token) {
        String userName = JwtHelper.getUserName(token);
        JSONArray result=new JSONArray();
        if (token != null && userName != null) {
            Example example=new Example(Address.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("userName",userName);
            result.addAll(addressMapper.selectByExample(example));
            Address address=addressMapper.queryDefaultAddress(userName);
            if (address!=null&&result.contains(address)){
                result.remove(result);
                result.add(0,address);
            }
            result.add(Utils.getOk());
            return result.toJSONString();
        }
        result.add(Utils.getError());
        result.add(Utils.getIllgalToken());
        return result.toJSONString();
    }

    //修改默认地址
    @ResponseBody
    @PostMapping("/updateDefaultAddress")
    public String updateDefaultAddress(@RequestHeader(value = "Authorization") String token,
                                       Integer addressId) throws JSONException {
        String userName = JwtHelper.getUserName(token);
        JSONObject result = new JSONObject();
        if (userName != null && addressId != null) {
            Example example=new Example(Address.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("userName",userName);
            criteria.andEqualTo("defaultAddress","是");
            Address dbAddress=addressMapper.selectOneByExample(example);
            if (dbAddress!=null)addressMapper.cancelDefaultAddress(userName);
            if (dbAddress!=null) {
                int i = addressMapper.updateDefaultAddress(addressId);
                if (i > 0) {
                    result.put("msg", "ok");
                    return result.toString();
                }
            }
        }
        result.put("msg", "error");
        return result.toString();
    }

    //删除地址
    @ResponseBody
    @PostMapping("/deleteAddress")
    public String deleteAddress(@RequestHeader(value = "Authorization") String token,
                                Integer addressId) throws JSONException {
        String userName = JwtHelper.getUserName(token);
        JSONObject result = new JSONObject();
        if (userName == null) {
            result.put("msg", "error");
            result.put("error", "illegal token");
            return result.toString();
        }
        if (addressId == null) {
            result.put("msg", "error");
            result.put("error", "addressId is null");
        }
        int n = addressMapper.deleteByPrimaryKey(addressId);
        if (n > 0) {
            result.put("msg", "ok");
            return result.toString();
        } else {
            result.put("msg", "error");
            return result.toString();
        }
    }

    //新增地址
    @PostMapping("/addUserAddress")
    @ResponseBody
    public String addUserAddress(@RequestHeader(value = "Authorization") String token,  String fullName ,
                                 String tel, String address, String defaultAddress) throws JSONException {
        String userName = JwtHelper.getUserName(token);
        JSONObject result = new JSONObject();
        if (userName == null) {
            result.put("msg", "error");
            result.put("error", "illegal token");
            return result.toString();
        } else if (fullName == null || tel == null || address == null) {
            result.put("msg", "error");
            return result.toString();
        } else {
            Address userAddress = new Address();
            userAddress.setUserName(userName);
            userAddress.setAddress(address);
            userAddress.setFullName(fullName);
            userAddress.setTel(tel);
            if (defaultAddress != null && defaultAddress.equals("是")) {
                int n =-1;
                if (addressMapper.queryDefaultAddress(userName)!=null)
                n=addressMapper.cancelDefaultAddress(userName);
                if (n > 0||n==-1) {
                    userAddress.setDefaultAddress(defaultAddress);
                } else {
                    result.put("msg", "error");
                    return result.toString();
                }
            } else {
                userAddress.setDefaultAddress("否");
            }
            int i = addressMapper.insert(userAddress);
            if (i > 0) {
                result.put("msg", "ok");
                return result.toString();
            } else {
                result.put("msg", "error");
                return result.toString();
            }
        }
    }

}
