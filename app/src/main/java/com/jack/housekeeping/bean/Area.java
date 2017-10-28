package com.jack.housekeeping.bean;

import com.google.gson.Gson;

/**
 * Created by admin
 * Class Describe :
 * <p>
 * on 2017/10/28.
 */

public class Area {
    /**
     * area_id : 1
     * area_name : 犀浦
     * area_time : 2017-10-10 16:01:24
     * area_manage : 456
     * area_info : 无
     */

    private int area_id;
    private String area_name;
    private String area_time;
    private String area_manage;
    private String area_info;

    public static Area objectFromData(String str) {

        return new Gson().fromJson(str, Area.class);
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getArea_time() {
        return area_time;
    }

    public void setArea_time(String area_time) {
        this.area_time = area_time;
    }

    public String getArea_manage() {
        return area_manage;
    }

    public void setArea_manage(String area_manage) {
        this.area_manage = area_manage;
    }

    public String getArea_info() {
        return area_info;
    }

    public void setArea_info(String area_info) {
        this.area_info = area_info;
    }
}
