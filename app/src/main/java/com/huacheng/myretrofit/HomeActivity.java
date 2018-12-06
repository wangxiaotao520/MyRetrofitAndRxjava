package com.huacheng.myretrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huacheng.myretrofit.dialog.SmallDialog;
import com.huacheng.myretrofit.http.ApiHttpClient;
import com.huacheng.myretrofit.http.BaseObserver;
import com.huacheng.myretrofit.http.BaseResponse;
import com.huacheng.myretrofit.http.ExceptionHandle;
import com.huacheng.myretrofit.http.RetrofitClient;
import com.huacheng.myretrofit.http.apiservice.MyApiService;
import com.huacheng.myretrofit.http.download.DownloadCallBack;
import com.huacheng.myretrofit.http.upload.FileUploadObserver;
import com.huacheng.myretrofit.model.ModelCircleDetail;
import com.huacheng.myretrofit.model.ModelItemBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * created by wangxiaotao
 * retrofit+gson解析的坑
 * 1.要想正常使用retrofit+gson解析，必须使用普通请求的方式 但是后台必须严格控制返回的数据类型，比如status不等于1的时候data也要是对应的格式 建议使用通用的方式
 * 2.通用的get 和post的请求 必须使用String的方式
 *
 *
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private SmallDialog smallDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        smallDialog = new SmallDialog(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        findViewById(R.id.tv_get).setOnClickListener(this);
        findViewById(R.id.tv_post).setOnClickListener(this);
        findViewById(R.id.tv_upload).setOnClickListener(this);
        findViewById(R.id.tv_download).setOnClickListener(this);
        findViewById(R.id.tv_json).setOnClickListener(this);
        findViewById(R.id.tv_myApi).setOnClickListener(this);
        findViewById(R.id.tv_normal_get).setOnClickListener(this);
        findViewById(R.id.tv_normal_post).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get:
                executeGet();
                break;
            case R.id.tv_post:
                executePost();
                break;
            case R.id.tv_upload:
                initProgressDialog();
                executeUpload();
                break;
            case R.id.tv_download:
                initProgressDialog();
                executeDownload();
                break;
            case R.id.tv_json:
                executeJson();
                break;
            case R.id.tv_myApi:
                executeMyApi();
                break;
            case R.id.tv_normal_get:
                executeNormalGet();
                break;
            case R.id.tv_normal_post:
                executeNormalPost();
                break;
            default:
                break;
        }
    }

    /**
     * 执行自己的Api
     */
    private void executeMyApi() {
        MyApiService service = RetrofitClient.getInstance(HomeActivity.this, ApiHttpClient.BASE_URL).create(MyApiService.class);
            smallDialog.show();
        // execute and add observable
        RetrofitClient.getInstance(HomeActivity.this).execute(

                service.getMyApiData("532","0"), new BaseObserver<BaseResponse<ModelCircleDetail>>(HomeActivity.this) {

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        Log.e("Lyk", e.getMessage());
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        smallDialog.dismiss();
                    }

                    @Override
                    public void OnNext(BaseResponse<ModelCircleDetail> baseResponse) {
                        smallDialog.dismiss();
                        if (baseResponse.isSuccess()){

                        }else {

                        }
                        Toast.makeText(HomeActivity.this, baseResponse.getMsg(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }


                });
    }

    /**
     * 上传json
     */
    private void executeJson() {
        //todo 无法测试
//        Map<String, String> maps = new HashMap<String, String>();
//
//        maps.put("ip", "21.22.11.33");
//        //"http://ip.taobao.com/service/getIpInfo.php?ip=21.22.11.33";
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
    }

    /**
     * 执行下载
     */
    private void executeDownload() {
        progressDialog.show();
        String url = "http://down.hui-shenghuo.cn/apk/HuiServers.apk";
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().myDownLoad(url, new DownloadCallBack() {
            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSucess(String path, String name, long fileSize) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, path+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(final long currentBytes, final long totalBytes) {
                super.onProgress(currentBytes, totalBytes);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setTitle("");
                        progressDialog.setMessage("正在下载");
                        progressDialog.setProgress((int) ((int) (currentBytes * 100 / totalBytes)));
                    }
                });
            }
        });
    }

    /**
     *进度条对话框
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("正在加载");
        //设置弹窗标题
        progressDialog.setTitle("进度条");
        //设置弹窗图标
      //  progressDialog.setIcon(R.drawable.ic_audiotrack_light);
        // 能够返回
        progressDialog.setCancelable(true);
        // 点击外部返回
        progressDialog.setCanceledOnTouchOutside(true);
        //设置进度条
        progressDialog.setProgress(100);
        progressDialog.setMax(100);
        //设置进度条是否明确
    //    progressDialog.setIndeterminate(true);
        //设置进度条样式
        //ProgressDialog.STYLE_SPINNER 环形精度条
        //ProgressDialog.STYLE_HORIZONTAL 水平样式的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


    }

    /**
     * 上传
     */
    private void executeUpload() {
     //   smallDialog.show();
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("community_id", "3");
        params.put("c_id", "130");
        params.put("img_num", 4 + "");
        params.put("content", "5oiR5rWL6K+V5LqG\n");
        ArrayList<File> files = new ArrayList<>();

        for (int i1 = 0; i1 < 4; i1++) {
            File file = new File("/storage/emulated/0/MagazineUnlock/magazine-unlock-01-2.3.1186-_B0B003B3BDA3801442EEABCDCC5CA3D4.jpg");
            files.add(file);
        }
        Map<String, File> params_files = new HashMap<String, File>();
            for (int i1 = 0; i1 < 4; i1++) {
                params_files.put("img"+i1,files.get(i1));
            }
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().myUpload("social/social_save/", params, params_files, new FileUploadObserver<String>() {
            @Override
            public void onUploadSuccess(String responseBody) {
                smallDialog.dismiss();
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, responseBody+"", Toast.LENGTH_LONG).show();
                Log.e("RetrofitClient", responseBody );
            }

            @Override
            public void onUploadFail(Throwable e) {
                smallDialog.dismiss();
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(final int index, final int persent, int total_persent) {
                //每个图片的上传的的进度
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.setTitle("第"+(index+1)+"张");
//                        progressDialog.setMessage("正在上传");
//                        progressDialog.setProgress((int) (persent));
//                    }
//                });
        //

              // 总的进度
                progressDialog.setProgress((int) (total_persent));
            }

            @Override
            public void onSubscribe(Disposable d) {

            }
        });
    }

    /**
     * 执行通用post请求
     */
    private void executePost() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "532");
        params.put("is_pro", "0");
        smallDialog.show();
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().post(ApiHttpClient.GET_HOUSETYPE
                , params, new BaseObserver<String>(this) {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }


                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        smallDialog.dismiss();
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void OnNext(String s) {
                        smallDialog.dismiss();

                       Toast.makeText(HomeActivity.this, s+"", Toast.LENGTH_LONG).show();
                    }


                });
    }

    /**
     * 执行普通Post请求
     */
    private void executeNormalPost() {
        Map<String, String> params = new HashMap<String, String>();
       params.put("id", "532");
       params.put("is_pro", "0");
        smallDialog.show();
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().getPostData(params, new BaseObserver<ModelCircleDetail>(this) {


            @Override
            public void onSubscribe(Disposable d) {

            }


            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                smallDialog.dismiss();
                Toast.makeText(HomeActivity.this, e.message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnNext(ModelCircleDetail data) {
                smallDialog.dismiss();
                Toast.makeText(HomeActivity.this, data.getNickname() + "", Toast.LENGTH_LONG).show();

            }


        });
    }

    /**
     * 执行普通get请求
     */
    private void executeNormalGet() {

        smallDialog.show();
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().getData(new BaseObserver<List<ModelItemBean>>(this) {


            @Override
            public void onSubscribe(Disposable d) {

            }


            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                smallDialog.dismiss();
                Toast.makeText(HomeActivity.this, e.message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnNext(List<ModelItemBean> modelItemBeans) {
                smallDialog.dismiss();
                Toast.makeText(HomeActivity.this, modelItemBeans.get(0).getStatus() + "", Toast.LENGTH_LONG).show();
            }

        });

    }

    /**
     * 执行通用get请求
     */
    private void executeGet() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("p", "1");
        smallDialog.show();
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().get(ApiHttpClient.GET_DEFAULT
                , params, new BaseObserver<String>(this) {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        smallDialog.dismiss();
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void OnNext(String s) {
                        smallDialog.dismiss();

                        Toast.makeText(HomeActivity.this, s, Toast.LENGTH_LONG).show();
                    }

                });
    }
}
