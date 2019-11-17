package com.sea.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

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
