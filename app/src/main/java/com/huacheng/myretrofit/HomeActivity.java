package com.huacheng.myretrofit;

import android.Manifest;
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
import com.huacheng.myretrofit.utils.TCFrequeControl;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
    private RxPermissions rxPermission;
    private TCFrequeControl frequeControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        smallDialog = new SmallDialog(this);
        rxPermission = new RxPermissions(HomeActivity.this);
        initView();
        ApiHttpClient.setTokenInfo("457704e558b4dda166699b2b7205c37400999652","5fa0ff809becf0a09932a9496b602ae5");
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
//                rxPermission.requestEach(
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .subscribe(new Consumer<Permission>() {
//                            @Override
//                            public void accept(Permission permission) throws Exception {
//                                if (permission.granted) {
//                                    // 用户已经同意该权限
//
//                                    executeUpload();
//                                } else if (permission.shouldShowRequestPermissionRationale) {
//                                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                                    Toast.makeText(HomeActivity.this, "权限拒绝", Toast.LENGTH_LONG).show();
//                                } else {
//                                    // 用户拒绝了该权限，并且选中『不再询问』
//                                    Toast.makeText(HomeActivity.this, "权限拒绝[不再询问]", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });

                rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                               Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isGranted) throws Exception {
                        if (isGranted){
                            executeUpload();
                        }else {
                            Toast.makeText(HomeActivity.this, "权限拒绝", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.tv_download:
                initProgressDialog();

//                rxPermission.requestEach(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .subscribe(new Consumer<Permission>() {
//                            @Override
//                            public void accept(Permission permission) throws Exception {
//                                if (permission.granted) {
//                                    // 用户已经同意该权限
//                                    Log.e("retrofit", "permission.granted>>>>");
//
//                                } else if (permission.shouldShowRequestPermissionRationale) {
//                                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                                    Toast.makeText(HomeActivity.this, "权限拒绝", Toast.LENGTH_LONG).show();
//                                } else {
//                                    // 用户拒绝了该权限，并且选中『不再询问』
//                                    Toast.makeText(HomeActivity.this, "权限拒绝[不再询问]", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });

                rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isGranted) throws Exception {
                        //多个权限只有都允许的情况下才为true
                        if (isGranted){
                         executeDownload();
                            Log.e("retrofit", "permission.granted>>>>");
                        }else {
                            Toast.makeText(HomeActivity.this, "权限拒绝", Toast.LENGTH_LONG).show();
                            Log.e("retrofit", "permission.granted>>>>");
                        }
                    }
                });

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
        Log.e("retrofit", "executeDownload:>>>>");
        progressDialog.show();
        String url = "http://down.hui-shenghuo.cn/apk/HuiServers.apk";
        RetrofitClient.getInstance(HomeActivity.this).myDownLoad(url, new DownloadCallBack() {
            @Override
            public void onError(final Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onSucess(final String path, String name, long fileSize) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, path+"", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onProgress(final long currentBytes, final long totalBytes) {
                if(frequeControl==null){
                    frequeControl= new TCFrequeControl();
                    frequeControl.init(1,1.5f);
                }
                if (frequeControl.canTrigger()==false){
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setTitle("");
                        progressDialog.setMessage("正在下载");
                        progressDialog.setProgress( ((int) (currentBytes * 100 / totalBytes)));
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
        params.put("community_id", "1");
        params.put("c_id", "130");
        params.put("img_num", 9 + "");
        params.put("content", "5oiR5rWL6K+V5LqG\n");
        ArrayList<File> files = new ArrayList<>();

        for (int i1 = 0; i1 < 9; i1++) {
            File file = new File("/storage/emulated/0/DCIM/Camera/298fd0ad79be48b99946b7b52af89d96.jpg");
            files.add(file);
        }
        Map<String, File> params_files = new HashMap<String, File>();
            for (int i1 = 0; i1 < 9; i1++) {
                params_files.put("img"+i1,files.get(i1));
            }
        RetrofitClient.getInstance(HomeActivity.this).myUpload("social/social_save/", params, params_files, new FileUploadObserver<String>() {
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
            public void onProgress(final int persent) {
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
                progressDialog.setProgress((int) (persent));
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
        RetrofitClient.getInstance(HomeActivity.this).createBaseApi().post(ApiHttpClient.GET_DEFAULT
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
