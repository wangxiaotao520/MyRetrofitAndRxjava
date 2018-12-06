package com.huacheng.myretrofit.http.apiservice;

import com.huacheng.myretrofit.http.BaseResponse;
import com.huacheng.myretrofit.model.ModelCircleDetail;
import com.huacheng.myretrofit.model.ModelItemBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wangxiaotao on 2018-12-05.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public interface BaseApiService {


    /**
     *普通写法
     */
    @GET("secondHouseType/getdefault")
    Observable<BaseResponse<List<ModelItemBean>>> getData();
    /**
     *普通写法
     */
    @POST("social/get_social/")
    Observable<BaseResponse<ModelCircleDetail>>getPostData( @QueryMap Map<String, String> maps);

    @GET("{url}")
    Observable<String> executeGet(@Path("url") String url, @QueryMap Map<String, String> maps);


    @POST("{url}")
    Observable<String> executePost(@Path("url") String url,
          //  @Header("") String authorization,
                                                  @QueryMap Map<String, String> maps);

    @POST("{url}")
    Observable<ResponseBody> json(
            @Path("url") String url,
            @Body RequestBody jsonStr);



    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    //我自己写的上传文件带参数
    @POST
    Observable<String> uploadFile(@Url String url, @Body MultipartBody body);
}
