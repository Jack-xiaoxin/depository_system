package com.example.depository_system.app;

import android.app.Application;
import android.content.Context;

// 全局唯一Application
public class AccountApplication extends Application {

    private static AccountApplication mInstance;

    public static float screenDensity;

    public static Context getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //初始化greendao
//        DbHelper.getInstance().init(this);

    }

//    public static DBManager<AccountModel, Long> getDbManager() {
//        return DbHelper.getInstance().author();
//    }
}
