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

    public static Object object;//反射机制返回的类

    public static boolean verifyAndType(ResponseBody responseBody, Type type){
        boolean isTure = true;
        try {
            String responseStr = responseBody.string();
            KLog.i(responseStr);
            JSONObject jsonObject = new JSONObject(responseStr);
            int code = jsonObject.getInt("status");
            switch (code){
                case 200:
                    String response = jsonObject.getString("message");//获取返回信息
                    KLog.i(response);
                    object = GsonFactory.getInstence().fromJson(response,type);//转换成相应类
                    break;
                case 404:
                    ToastUtil.getInstance().log("服务器出错了~");
                    isTure = false;
                    break;
           }
        } catch (Exception e) {
            e.printStackTrace();
            KLog.i("编译错误");
            isTure = false;
        }
        return isTure;
    }

    public static boolean verify(ResponseBody responseBody){
        boolean isTure = true;
        try {
            String responseStr = responseBody.string();
            KLog.i(responseStr);
            JSONObject jsonObject = new JSONObject(responseStr);
            int code = jsonObject.getInt("status");
            if (code!=200)
                isTure = false;
        } catch (Exception e) {
            e.printStackTrace();
            KLog.i("网络错误");
            isTure = false;
        }
        return isTure;
    }
}
