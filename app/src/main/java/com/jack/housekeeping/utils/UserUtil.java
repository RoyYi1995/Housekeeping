package com.jack.housekeeping.utils;

/**
 * Created by admin
 * Class Describe :
 * <p> 保存当前用户
 * on 2017/10/28.
 */

public class UserUtil {
    private static Object user;

    public static void setUser(Object user){
        UserUtil.user = user;
    }
    public static Object getCurrentUser(){
        return user;
    }
}
