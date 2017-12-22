package com.wanandroid;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanandroid.utils.StatusBarUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WanApplication extends Application {

    private static final String DB_NAME = "wanandroid.db";

    public static LiteOrm liteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        initBuyly();
        initLiteOrm();
        registerActivityLifecycleCallbacks(new StatusBarUtil());
    }

    private void initLiteOrm() {
        DataBaseConfig config = new DataBaseConfig(this);
        config.debugged = BuildConfig.LOG_DEBUG;
        config.dbName = DB_NAME;
        config.dbVersion = 1;
        config.onUpdateListener = null;
        liteOrm = LiteOrm.newSingleInstance(config);
    }

    private void initBuyly() {
        if (BuildConfig.LOG_DEBUG) {
            return;
        }
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        Bugly.init(context, BuildConfig.Buyly_App_id, BuildConfig.LOG_DEBUG, strategy);
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
