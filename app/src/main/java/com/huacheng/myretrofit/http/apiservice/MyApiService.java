package com.huacheng.myretrofit.http.apiservice;

import com.huacheng.myretrofit.http.BaseResponse;
import com.huacheng.myretrofit.model.ModelCircleDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Description: 自己写的apiservice
 * created by wangxiaotao
 * 2018/12/6 0006 下午 4:12
 */
public interface MyApiService {

    /**
     *普通写法
     */
    @GET("social/get_social/")
    Observable<BaseResponse<ModelCircleDetail>> getMyApiData(@Query("id") String id,@Query("is_pro") String is_pro);
}
