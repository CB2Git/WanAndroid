package com.wanandroid.widget;


import android.app.AlertDialog;
import android.content.Context;

import com.wanandroid.R;

/**
 * 自定义的等待对话框
 */
public class CusProgressDialog {


    public static AlertDialog show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.show();
        dialog.setContentView(R.layout.dialog_progress);
        return dialog;
    }
}