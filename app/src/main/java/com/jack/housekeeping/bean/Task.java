package com.jack.housekeeping.bean;

import com.google.gson.Gson;

/**
 * Created by Roy
 * Class Describe :
 * <p> 任务实例
 * on 2017/10/28.
 */

public class Task {
    /**
     * task_id : 4
     * task_name : null
     * task_customer : null
     * task_info : null
     * task_time : 2017-10-28 16:39:35
     * task_money : null
     * task_area : null
     * task_phone : null
     * task_state : 1
     * task_people : null
     * task_state_id : 1
     * task_state_name : 待接受
     */

    private int task_id;
    private Object task_name;
    private Object task_customer;
    private Object task_info;
    private String task_time;
    private Object task_money;
    private Object task_area;
    private Object task_phone;
    private int task_state;
    private Object task_people;
    private int task_state_id;
    private String task_state_name;

    public static Task objectFromData(String str) {

        return new Gson().fromJson(str, Task.class);
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public Object getTask_name() {
        return task_name;
    }

    public void setTask_name(Object task_name) {
        this.task_name = task_name;
    }

    public Object getTask_customer() {
        return task_customer;
    }

    public void setTask_customer(Object task_customer) {
        this.task_customer = task_customer;
    }

    public Object getTask_info() {
        return task_info;
    }

    public void setTask_info(Object task_info) {
        this.task_info = task_info;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public Object getTask_money() {
        return task_money;
    }

    public void setTask_money(Object task_money) {
        this.task_money = task_money;
    }

    public Object getTask_area() {
        return task_area;
    }

    public void setTask_area(Object task_area) {
        this.task_area = task_area;
    }

    public Object getTask_phone() {
        return task_phone;
    }

    public void setTask_phone(Object task_phone) {
        this.task_phone = task_phone;
    }

    public int getTask_state() {
        return task_state;
    }

    public void setTask_state(int task_state) {
        this.task_state = task_state;
    }

    public Object getTask_people() {
        return task_people;
    }

    public void setTask_people(Object task_people) {
        this.task_people = task_people;
    }

    public int getTask_state_id() {
        return task_state_id;
    }

    public void setTask_state_id(int task_state_id) {
        this.task_state_id = task_state_id;
    }

    public String getTask_state_name() {
        return task_state_name;
    }

    public void setTask_state_name(String task_state_name) {
        this.task_state_name = task_state_name;
    }
}
