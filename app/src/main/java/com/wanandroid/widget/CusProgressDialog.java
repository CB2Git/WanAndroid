package com.wanandroid.widget;


import android.app.AlertDialog;
import android.content.Context;

import com.wanandroid.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 自定义的等待对话框
 */
public class CusProgressDialog {

    public static AlertDialog show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.show();
        dialog.setContentView(R.layout.dialog_progress);
        AVLoadingIndicatorView view = dialog.findViewById(R.id.classify_one_cid_avi_indicator);
        view.show();
        return dialog;
    }
}