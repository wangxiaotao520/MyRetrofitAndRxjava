package com.huacheng.myretrofit.http;


/**
 * 网络返回基类 支持泛型
 * Created by wangxiaotao on 2018-12-05.
 */
public class BaseResponse<T> {

    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int code) {
        this.status = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return status ==1;
    }

    /**
     * 登录失效
     * @return
     */
    public boolean isOverTime(){
        return status ==2;
    }

}
