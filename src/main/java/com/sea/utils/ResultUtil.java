package com.sea.utils;

import java.util.List;

public class ResultUtil {

    public static Result error(ResultEnum resultEnum){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(null);
        return result;
    }

    public static Result error(ResultEnum resultEnum, String defaultMessage){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(defaultMessage);
        result.setData(null);
        return result;
    }

    public static Result error(ResultEnum resultEnum, List<String> defaultMessage){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(defaultMessage.toString());
        result.setData(null);
        return result;
    }

                                    //int code
    public static Result error(String defaultMessage) {
        Result result = new Result();
        result.setCode(result.getCode());
        result.setMsg(defaultMessage);
        result.setData(null);
        return result;
    }

    public static Result error(ResultEnum resultEnum, Object data) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(data);
        return result;
    }




    public static Result success(ResultEnum resultEnum){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(null);
        return result;
    }

    public static Result success(ResultEnum resultEnum, String defaultMessage){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(defaultMessage);
        result.setData(null);
        return result;
    }

    public static Result success(ResultEnum resultEnum, Object data){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(data);
        return result;
    }

    //int code,
    public static Result success(String defaultMessage) {
        Result result = new Result();
        result.setCode(result.getCode());
        result.setMsg(defaultMessage);
        result.setData(null);
        return result;
    }
}
