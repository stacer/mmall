package com.mmall.common;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-10:39
 */
public enum  ResponseCode {

    /**
     * 把每个枚举想成是构造方法的另一种表达
     * ResponseCode.SUCCESS-->ResponseCode.success(0,"SUCCESS")
     * -->ResponseCode(int code,String desc)
     */

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final Integer code;
    private final String desc;

    ResponseCode(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }

}
