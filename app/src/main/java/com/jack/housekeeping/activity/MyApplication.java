package com.jack.housekeeping.activity;

import android.app.Application;

import com.jack.housekeeping.utils.ToastUtil;

/**
 * Created by admin
 * Class Describe :
 * <p>
 * on 2017/10/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        ToastUtil.getInstance().init(getApplicationContext());
        super.onCreate();
    }
}
