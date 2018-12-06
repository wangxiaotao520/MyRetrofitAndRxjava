package com.huacheng.myretrofit.http.download;

public abstract class DownloadCallBack {
    public void onStart(){}

    public void onCompleted(){}

    abstract public void onError(Throwable e);

    public void onProgress(long currentBytes, long totalBytes){}

    abstract public void onSucess(String path, String name, long fileSize);
}