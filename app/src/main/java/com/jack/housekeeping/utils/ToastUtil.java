package com.jack.housekeeping.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin
 * Class Describe :
 * <p> 自定义打印格式
 * on 2017/10/24.
 */

public class ToastUtil {

    private  Context context;

    private static ToastUtil toastUtil;

    private ToastUtil() {
    }

    public static ToastUtil getInstance(){
        if (toastUtil == null){
            toastUtil = new ToastUtil();
        }
        return toastUtil;
    }

    public void init(Context context){
        this.context = context;
    }

    public void log(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
