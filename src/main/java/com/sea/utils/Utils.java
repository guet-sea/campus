package com.sea.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private final static String OK="ok";

    private final static String ERROR="error";

    private final static String MSG="msg";

    private final static String ILLEGAL_TOKEN="illegal token";

    private final static String PARAM_MISSING="param missing";

    private final static String PARAM_DOES_NOT_CORRESPOND="parameter does not correspond";



    public final static Map<String,String> getOk(){
        Map<String,String> map=new HashMap<>();
        map.put(MSG,OK);
        return map;
    }
    public final static Map<String,String> getNotCorrespond(){
        Map<String,String> map=new HashMap<>();
        map.put(ERROR,PARAM_DOES_NOT_CORRESPOND);
        return map;
    }

    public final static Map<String,String> getParamMiss(){
        Map<String,String> map=new HashMap<>();
        map.put(ERROR,PARAM_MISSING);
        return map;
    }

    public final static Map<String,String> getError(){
        Map<String,String> map=new HashMap<>();
        map.put(MSG,ERROR);
        return map;
    }

    public final static Map<String,String> getIllgalToken(){
        Map<String,String> map=new HashMap<>();
        map.put(ERROR,ILLEGAL_TOKEN);
        return map;
    }

    //模糊匹配
    public final static String getLike(String original){
        String to="%"+original+"%";
        return to;
    }

    //SHA加密
    public final static String getEncode(String input) {
        try {
            //使用SHA算法加密
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(input.getBytes());
            String encode = new BigInteger(md.digest()).toString(32);
            System.out.println(encode);
            return encode;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("加密失敗！");
        }
        return null;
    }
}
