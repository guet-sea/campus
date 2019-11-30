package com.sea.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtHelper {
    /**
     * APP登录Token的生成和解析
     * 
     */

    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    public static final String SECRET = "guetsea";

    /**
     * JWT生成Token.<br/>
     * 
     * JWT构成: header, payload, signature
     * 
     * @param userName
     *            登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(String userName) throws Exception {
        Date iatDate = new Date();
        long now=System.currentTimeMillis();
        long exp=now+1000*60*60*3;
        //过期时间
        Date expiresDate=new Date(exp);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "WEB").withClaim("userName", null == userName ? null : userName.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }

    /**
     * 解密Token
     * 
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
            jwt.getExpiresAt();
        } catch (Exception e) {
            // e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
            return null;
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token获取userName
     * 
     * @param token
     * @return userName
     */
    public static String getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_id_claim = claims.get("user_id");
        if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return user_id_claim.asString();
    }
    public static Date getExpiresTime(String token) {
    	DecodedJWT jwt = null;
    	Date expiresTime=null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
            expiresTime= jwt.getExpiresAt();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("token 校验失败");
            // token 校验失败, 抛出Token验证非法异常
        }
		return expiresTime;
		
	}
    
    public static  String  getUserName(String token) {
    	Map<String, Claim> claims = verifyToken(token);
        if (claims==null)return null;
        Claim userNameClaim = claims.get("userName");
        if (null == userNameClaim || StringUtils.isEmpty(userNameClaim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
            System.out.println("token 校验失败");
            return null;
        }
        return userNameClaim.asString();
		
	}

    public static void main(String[] args) {
        try {
            String token=JwtHelper.createToken("root");
//            String token="1eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJXRUIiLCJpc3MiOiJTZXJ2aWNlIiwidXNlck5hbWUiOiI2NjYiLCJleHAiOjE1NzQ4NDI3MDgsImlhdCI6MTU3NDgzMTkwOH0.VoDA2mSJy2Gi1CSh_HpEtxYvuZE9mP3yhKyR_iXa89g";
            System.out.println(token);
            System.out.println(JwtHelper.getUserName(token));
            Date s=null;
            s=JwtHelper.getExpiresTime(token);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
