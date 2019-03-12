package com.huacheng.myretrofit.http.upload;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class UpLoadProgressInterceptor implements Interceptor {
    private FileUploadObserver<String> fileUploadObserver;


    public UpLoadProgressInterceptor(FileUploadObserver<String> fileUploadObserver) {
        this.fileUploadObserver = fileUploadObserver;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(null == request.body()){
            return chain.proceed(request);
        }

        Request build = request.newBuilder()
                .method(request.method(),
                        new UploadFileRequestBody(request.body(),
                                fileUploadObserver))
                .build();
        return chain.proceed(build);
    }
}
