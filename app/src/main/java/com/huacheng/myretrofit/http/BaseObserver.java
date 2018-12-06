package com.huacheng.myretrofit.http;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;

public abstract class BaseObserver<T> implements Observer<T> {

    private Context context;
    private boolean isNeedCahe;

    public BaseObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        Log.e("Tamic", e.getMessage());
        // todo error somthing

        if(e instanceof ExceptionHandle.ResponeThrowable){
            onError((ExceptionHandle.ResponeThrowable)e);
        } else {
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }




    @Override
    public void onComplete() {

    //    Toast.makeText(context, "http is Complete", Toast.LENGTH_SHORT).show();
        // todo some common as  dismiss loadding
    }


    public abstract void onError(ExceptionHandle.ResponeThrowable e);
    public abstract void OnNext(T t);

    @Override
    public void onNext(T t) {
        if (t instanceof String){
            //TODO 使用String的方式 通用get和 post请求
            try {
                JSONObject jsonObject = new JSONObject((String) t);
                int status = jsonObject.getInt("status");
                if (status==2){
                    //TODO 登录失效
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        OnNext(t);
    }

}