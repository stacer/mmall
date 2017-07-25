package com.mmall.vo;

import com.mmall.common.ResponseCode;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-10:25
 */
//有null的时候不序列化
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private Integer status;
    private String msg;
    private T data;

    public Integer getStatus(){
        return status;
    }

    public String getMsg(){
        return msg;
    }

    public T getData(){
        return data;
    }

    //------------私有构造方法开始--------------
    private ServerResponse(Integer status){
        this.status = status;
    }

    private ServerResponse(Integer status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(Integer status,String msg,T data){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    private ServerResponse(Integer status,String msg){
        this.msg = msg;
        this.status = status;
    }

    //------------私有构造方法结束--------------


    @JsonIgnore
    //忽略
    public boolean success(){
        //使用枚举将魔法数字归类
        //return this.status == 0;
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    //-------------对外开放的方法开始------------
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMsg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(Integer errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
