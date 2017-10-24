package com.jack.housekeeping.utils;

import com.google.gson.Gson;

/**
 * Created by admin
 * Class Describe :
 * <p> Gson单例实现
 * on 2017/10/24.
 */

public class GsonFactory {

    private static Gson gson;

    private GsonFactory() {
    }

    public static Gson getInstence(){
        if (gson == null){
            gson = new Gson();
        }
        return gson;
    }

}
