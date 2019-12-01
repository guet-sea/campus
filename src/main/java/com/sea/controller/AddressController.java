package com.sea.controller;

import com.sea.bean.Address;
import com.sea.bean.Goods;
import com.sea.dao.AddressMapper;
import com.sea.utils.JwtHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/Address")
public class AddressController {

    @Autowired
    private AddressMapper addressMapper;

    @ResponseBody
    @RequestMapping("/getDefaultAddress")
    public String getDefaultAddress(@RequestHeader(value = "Authorization")String token) throws JSONException {
        //if (token==null)token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJXRUIiLCJpc3MiOiJTZXJ2aWNlIiwidXNlck5hbWUiOiJyb290IiwiZXhwIjoxNTc1MTk4OTE4LCJpYXQiOjE1NzUxODgxMTh9.f6sU9Qn-oBf-7Y_B-mfRc16yfyknZKcNYIgN4MSBdIs";
        String userName= JwtHelper.getUserName(token);
        JSONObject result=new JSONObject();
        if (userName!=null){
            Address address=addressMapper.queryDefaultAddress(userName);
            result.put("name",address.getName());
            result.put("address",address.getAddress());
            result.put("tel",address.getTel());
            return result.toString();
        }
        result.put("msg","error");
        return result.toString();
    }
}
