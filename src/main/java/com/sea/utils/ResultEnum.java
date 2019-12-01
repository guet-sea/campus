package com.sea.utils;

public enum ResultEnum {

    ERROR_TOKEN_ILLEGALE(707,"token非法"),
    ERROR_TOKEN_DISMATCH(706,"用户错误"),
    ERROR_PWDWRONG(702,"密码错误"),
    ERROR_NULL(701,"para error"),
    ERROR(400,"操作失败"),

    SUCCESS(200,"操作成功"),
    SUCCESS_WITHOUDATA(601,"no data");

    private Integer code;

    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
