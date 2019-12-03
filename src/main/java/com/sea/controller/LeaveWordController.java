package com.sea.controller;

import com.sea.bean.LeaveWord;
import com.sea.bean.User;
import com.sea.dao.LeaveWordMapper;
import com.sea.dao.UserMapper;
import com.sea.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/LeaveWord")
public class LeaveWordController {

    @Autowired
    private LeaveWordMapper leaveWordMapper;

    @Autowired
    private UserMapper userMapper;

    //查看留言可以不需要用户校验
    @ResponseBody
    @PostMapping("/getGoodsLeaveWord")
    public List<LeaveWord> getGoodsLeaveWord(Integer goodsId){
        if (goodsId!=null){
//<<<<<<< HEAD
            List<LeaveWord> leaveWords=leaveWordMapper.enquiryGoodsLeaveWord(goodsId);
//            Example example=new Example(LeaveWord.class);
//            Example.Criteria criteria=example.createCriteria();
//            criteria.andEqualTo("goodsId",goodsId);
//            List<LeaveWord> leaveWords=leaveWordMapper.selectByExample(example);
//=======
//            //List<LeaveWord> leaveWords=leaveWordMapper.enquiryGoodsLeaveWord(goodsId);
//            Example example=new Example(LeaveWord.class);
//            Example.Criteria criteria=example.createCriteria();
//            criteria.andEqualTo("goodsId",goodsId);
//            //List<LeaveWord> leaveWords=leaveWordMapper.selectByExample(example);
//            List<LeaveWord> leaveWords = leaveWordMapper.enquiryGoodsLeaveWord(goodsId);
//>>>>>>> 028ae90224894f66abb6e7b622b9314738ba7cf7
            for (int i=0;i<leaveWords.size();i++){
                String userName=leaveWords.get(i).getUserName();
                User user=userMapper.selectByPrimaryKey(userName);
                leaveWords.get(i).setHeadPortrait(user.getHeadPortrait());
            }
            return leaveWords;
        }
        return null;
    }


    @ResponseBody
    @PostMapping("/sendGoodsLeaveWord")
    public Result<Object> sendGoodsLeaveWord(@RequestHeader(value = "Authorization")String token,
                                             @Validated @RequestBody LeaveWord leaveWord,
                                             BindingResult ValidateRs){
        //用户校验
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR_TOKEN_ILLEGALE);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR_TOKEN_DISMATCH);
        }

        //非空校验
        if(ValidateRs.hasErrors()){
            List<String> errors = new ArrayList<>();
            List<ObjectError> allErrors = ValidateRs.getAllErrors();
            for (ObjectError error: allErrors){
                errors.add(error.getDefaultMessage());
            }
            return ResultUtil.error(ResultEnum.ERROR,errors);
        }
        //后端生成留言时间
        String msgTime = DateUtil.getyyyyMMddHHmmss();
        leaveWord.setMessageTime(msgTime);
        int inserRs = leaveWordMapper.insert(leaveWord);
        if(inserRs > 0){
            return ResultUtil.success(ResultEnum.SUCCESS,leaveWord);
        }else {
            return ResultUtil.error(ResultEnum.ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/deleteGoodsLeaveWord")
    public Result<Object> deleteGoodsLeaveWord(@RequestHeader(value = "Authorization")String token, int id){
        //用户校验
        String userName= JwtHelper.getUserName(token);
        if (userName==null){
            return ResultUtil.error(ResultEnum.ERROR_TOKEN_ILLEGALE);
        }
        User dbUser=userMapper.selectByPrimaryKey(userName);
        if (dbUser==null){
            return ResultUtil.error(ResultEnum.ERROR_TOKEN_DISMATCH);
        }else{
            Example example = new Example(LeaveWord.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",id);
            criteria.andEqualTo("userName",dbUser.getUserName());
            int result = leaveWordMapper.deleteByExample(example);
            if(result>0){
                return ResultUtil.success(ResultEnum.SUCCESS);
            }else {
                return ResultUtil.error(ResultEnum.ERROR);
            }
        }
    }
}
