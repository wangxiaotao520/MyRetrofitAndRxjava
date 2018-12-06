package com.huacheng.myretrofit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.huacheng.myretrofit.R;


/**
 * Description: 打开页面时的等待对话框
 * created by wangxiaotao
 * 2018/6/20 0020 下午 5:08
 */
public class SmallDialog extends Dialog{



    private TextView tipTextView;

    public SmallDialog(@NonNull Context context) {
        super(context, R.style.my_dialog);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_small_dialog);
        initView();
        initListener();
        // 设置对话框在窗口中显示的位置,因为对话框默认是显示在中心的
        // 得到对话框所显示在的窗口对象
        Window window = this.getWindow();
        // 得到窗口的布局参数对象，可以使用它来设置对话框在窗口中的布局参数
        WindowManager.LayoutParams params = window.getAttributes();

        // 设置对话框显示的位置
        params.gravity = Gravity.CENTER;

        params.width = params.WRAP_CONTENT;
        params.height = params.WRAP_CONTENT;

        window.setAttributes(params);
        initView();
    }

    private void initListener() {
    }

    private void initView() {
        tipTextView = findViewById(R.id.tipTextView);
    }

    public void setTipTextView(String tipText) {
        this.tipTextView .setText(tipText);
    }
}
