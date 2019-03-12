package com.huacheng.myretrofit.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.huacheng.myretrofit.http.apiservice.BaseApiService;
import com.huacheng.myretrofit.http.cookie.NovateCookieManger;
import com.huacheng.myretrofit.http.download.DownLoadManager;
import com.huacheng.myretrofit.http.download.DownloadCallBack;
import com.huacheng.myretrofit.http.download.FileResponseBody;
import com.huacheng.myretrofit.http.upload.FileUploadObserver;
import com.huacheng.myretrofit.http.upload.UpLoadProgressInterceptor;
import com.huacheng.myretrofit.model.ModelCircleDetail;
import com.huacheng.myretrofit.model.ModelItemBean;
import com.huacheng.myretrofit.model.ModelSelectCommon;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * RetrofitClient
 * Created by wangxiaotao
 */
public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;
    private BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = ApiHttpClient.BASE_URL;
    private static Context mContext;

    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    // .addConverterFactory(ResponseConvertFactory.create())
                    //TODO 支持String解析
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //todo 支持gson解析
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl);
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);


    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
                mContext);
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }

        return new RetrofitClient(context, url);
    }

    public static RetrofitClient getInstance(Context context, String url, Map<String, String> headers) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitClient(context, url, headers);
    }

    private RetrofitClient() {

    }

    private RetrofitClient(Context context) {

        this(context, baseUrl, null);
    }

    private RetrofitClient(Context context, String url) {

        this(context, url, null);
    }

    private RetrofitClient(Context context, String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "tamic_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                if (TextUtils.isEmpty(message)) return;
                                String s = message.substring(0, 1);
                                //如果收到响应是json才打印
                                if ("{".equals(s) || "[".equals(s)) {
                                    //TODO 打印log的地方
                                  String json = message;
                                }
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(new NovateCookieManger(context))
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CaheInterceptor(context))
                .addNetworkInterceptor(new CaheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
               .addConverterFactory(GsonConverterFactory.create())
              //  .addConverterFactory(ResponseConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    /**
     * ApiBaseUrl
     *
     * @param newApiBaseUrl
     */
    public static void changeApiBaseUrl(String newApiBaseUrl) {
        baseUrl = newApiBaseUrl;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }

    /**
     * addcookieJar
     */
    public static void addCookie() {
        okHttpClient.newBuilder().cookieJar(new NovateCookieManger(mContext)).build();
        retrofit = builder.client(okHttpClient).build();
    }

    /**
     * ApiBaseUrl
     *
     * @param newApiHeaders
     */
    public static void changeApiHeader(Map<String, String> newApiHeaders) {

        okHttpClient.newBuilder().addInterceptor(new BaseInterceptor(newApiHeaders)).build();
        builder.client(httpClient.build()).build();
    }

    /**
     * create BaseApi  defalte ApiManager
     *
     * @return ApiManager
     */
    public RetrofitClient createBaseApi() {
        apiService = create(BaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * 普通get请求
     *
     * @param observer
     */
    public void getData(Observer<List<ModelItemBean>> observer) {
        //TODO 判断token是否为空

        apiService.getData()
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(observer);
    }

    /**
     * 普通post请求
     *
     * @param observer
     */
    public void getPostData(Map parameters, Observer<ModelCircleDetail> observer) {
        //TODO 判断token是否为空
        parameters.put("token", ApiHttpClient.TOKEN);
        parameters.put("tokenSecret", ApiHttpClient.TOKEN_SECRET);
        apiService.getPostData(parameters)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(observer);
    }

    public void get(String url, Map parameters, Observer observer) {
        //TODO 判断token是否为空
        parameters.put("token", ApiHttpClient.TOKEN);
        parameters.put("tokenSecret", ApiHttpClient.TOKEN_SECRET);
        apiService.executeGet(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerNormal())
                .subscribe(observer);
    }

    public void post(String url, Map<String, String> parameters, Observer<String> observer) {
        //TODO 判断token是否为空
        parameters.put("token", ApiHttpClient.TOKEN);
        parameters.put("tokenSecret", ApiHttpClient.TOKEN_SECRET);

        apiService.executePost(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformerNormal())
                .subscribe(observer);
    }

    public void json(String url, RequestBody jsonStr, Observer<ModelSelectCommon> observer) {

        apiService.json(url, jsonStr)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(observer);
    }


    //转换器
    ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {


            @Override
            public ObservableSource apply(Observable upstream) {
                return ((Observable) upstream).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer();
    }

    //
    public <T> ObservableTransformer<BaseResponse<T>, T> transformer() {

        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(Observable upstream) {
                return ((Observable) upstream).map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }


        };
    }

    public <T> ObservableTransformer<T, T> transformerNormal() {

        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(Observable upstream) {
                return ((Observable) upstream).onErrorResumeNext(new HttpResponseFunc<T>());
            }


        };
    }

    public <T> Observable<T> switchSchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {


        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }

    //普通方式的解析
    private class HandleFuc<T> implements Function<BaseResponse<T>, T> {

        @Override
        public T apply(BaseResponse<T> tBaseResponse) throws Exception {
            if (tBaseResponse.isSuccess()) {

                return tBaseResponse.getData();

            } else if (tBaseResponse.isOverTime()) {
                //TODO 根据我们公司的业务处理登录失效的逻辑

                throw new ExceptionHandle.ApiException(tBaseResponse.getStatus(),tBaseResponse.getMsg());
            } else {
                throw new ExceptionHandle.ApiException(tBaseResponse.getStatus(),tBaseResponse.getMsg());
            }
        }
    }


    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        return null;
    }


    /**
     * DownSubscriber
     *
     * @param <ResponseBody>
     */
    public class DownloadObserver<ResponseBody> implements Observer<ResponseBody> {
        DownloadCallBack callBack;

        public DownloadObserver(DownloadCallBack callBack) {
            this.callBack = callBack;
        }


        @Override
        public void onError(Throwable e) {
            if (callBack != null) {
                callBack.onError(e);
            }
        }

        @Override
        public void onComplete() {
            if (callBack != null) {
                callBack.onCompleted();
            }
        }

        @Override
        public void onSubscribe(Disposable d) {
            Log.e("retrofit", "Disposable:>>>>");
        }

        @Override
        public void onNext(final ResponseBody responseBody) {
            Log.e("retrofit", "onNext:>>>>");
            new Thread(new Runnable() {
                @Override
                public void run() {
                     DownLoadManager.getInstance(callBack).writeResponseBodyToDisk(mContext, (okhttp3.ResponseBody) responseBody);
                }
            }).start();


        }
    }

    /**
     * 我自己写的上传
     *
     * @param url
     * @param
     */
    public void myUpload(String url, Map<String, String> params, Map<String, File> files, FileUploadObserver<String> fileUploadObserver) {

        UpLoadProgressInterceptor interceptor = new UpLoadProgressInterceptor(fileUploadObserver);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        this.apiService=retrofit.create(BaseApiService.class);
        params.put("token", ApiHttpClient.TOKEN);
        params.put("tokenSecret", ApiHttpClient.TOKEN_SECRET);

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
//            int index = 0;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                //TODO 加了这么一条 ,这样获得的是每一个的进度
//                UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody( fileBody, fileUploadObserver);
//                index++;
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        //获取总长度
//        try {
//            fileUploadObserver.setAll_total_length(multipartBuilder.build().contentLength(), files.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        apiService.uploadFile(url, multipartBuilder.build())
                .compose(schedulersTransformer())
                .compose(transformerNormal())
                .subscribe(fileUploadObserver);
    }

    /**
     * 我自己写得下载
     *
     * @param url
     */
    public void myDownLoad(String url, final DownloadCallBack callBack) {
       OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response response = chain.proceed(chain.request());
                        //将ResponseBody转换成我们需要的FileResponseBody
                        return response.newBuilder().body(new FileResponseBody(response.body(), callBack)).build();
                    }
                }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        this.apiService = retrofit.create(BaseApiService.class);
        //
//        apiService
//                .downloadFile(url)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .map(new Function<ResponseBody, ResponseBody>() {
//
//                    @Override
//                    public ResponseBody apply(ResponseBody responseBody) throws Exception {
//                        return responseBody;
//                    }
//                }).observeOn(Schedulers.computation()) // 用于计算任务
//                .doOnNext(new Consumer<ResponseBody>() {
//                    @Override
//                    public void accept(ResponseBody responseBody) throws Exception {
//                        DownLoadManager.getInstance(callBack).writeResponseBodyToDisk(mContext, responseBody);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DownloadObserver<ResponseBody>(callBack));
        //
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DownloadObserver<ResponseBody>(callBack));


    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
