package com.jack.housekeeping.utils;

/**
 * Created by admin
 * Class Describe :
 * <p> 保存当前用户
 * on 2017/10/28.
 */

public class UserUtil {
    private static Object user;
    public static int CUSTOM_TYPE = 0;
    public static int EMPLOYEE_TYPE = 1;

    private static int type;

    public static void setCustomType(int type){
        UserUtil.type = type;
    }

    public static int getCurrentUserType(){
        return UserUtil.type;
    }

    public static void setUser(Object user){
        UserUtil.user = user;
    }
    public static Object getCurrentUser(){
        return user;
    }


}
