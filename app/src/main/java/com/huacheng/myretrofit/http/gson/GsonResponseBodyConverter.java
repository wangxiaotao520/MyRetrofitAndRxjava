package com.huacheng.myretrofit.http.gson;

import android.util.Log;

import com.google.gson.Gson;
import com.huacheng.myretrofit.http.BaseResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Charles on 2016/3/17.
 */
class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
            Log.d("Network", "response>>" + response);
            //httpResult 只解析result字段
        BaseResponse httpResult = gson.fromJson(response, BaseResponse.class);
            //
        //坑 gson只能进行一级解析，比如data 是list list里的就解析不了，
            return gson.fromJson(response, type);


    }
}
