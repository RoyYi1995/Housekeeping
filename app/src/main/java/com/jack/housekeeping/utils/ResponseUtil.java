package com.jack.housekeeping.utils;

import com.socks.library.KLog;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Created by admin
 * Class Describe :
 * <p> 验证后台回复code
 * on 2017/10/24.
 */

public class ResponseUtil {
    private static String message;
    // TODO: 2017/10/24 反射获取类实现需要修改
    public static Object getByType(Type type){
        Object object = null;//反射机制返回的类
        try {
            object = GsonFactory.getInstence().fromJson(message,type);//转换成相应类
        } catch (Exception e) {
            e.printStackTrace();
            KLog.i("类编译错误");
        }
        return object;
    }

    public static boolean verify(ResponseBody responseBody,boolean haveMsg){
        boolean isTure = true;
        try {
            String responseStr = responseBody.string();
            KLog.i(responseStr);
            JSONObject jsonObject = new JSONObject(responseStr);
            int code = jsonObject.getInt("status");
            if (code!=200)
                isTure = false;
            if (haveMsg){
                message = jsonObject.getString("data");
                KLog.i(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            KLog.i("网络错误");
            isTure = false;
        }
        return isTure;
    }
}
