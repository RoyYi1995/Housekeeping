package com.jack.housekeeping.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Custom;
import com.jack.housekeeping.bean.Employee;
import com.jack.housekeeping.bean.Task;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.jack.housekeeping.utils.ToastUtil;
import com.jack.housekeeping.utils.UserUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class TaskActivity extends AppCompatActivity {

    private final String ADD_TASK_URL = "/customer/addTask";
    private final String GET_TASK_URL = "/employee/employeeToTask";

    @BindView(R.id.task_name_et)
    EditText taskNameEt;
    @BindView(R.id.task_time_et)
    EditText taskTimeEt;
    @BindView(R.id.task_info_et)
    EditText taskInfoEt;
    @BindView(R.id.task_money_et)
    EditText taskMoneyEt;
    @BindView(R.id.task_people_et)
    EditText taskPeopleEt;
    @BindView(R.id.task_people_phone_et)
    EditText taskPeoplePhoneEt;
    @BindView(R.id.task_state_name_et)
    EditText taskStateNameEt;
    @BindView(R.id.focuce_ll)
    LinearLayout focuceLl;
    @BindView(R.id.add_task_btn)
    Button addTaskBtn;
    @BindView(R.id.get_task_btn)
    Button getTaskBtn;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        task = (Task) getIntent().getSerializableExtra("task");
        if (task != null) {
            setData();
            if (task.getTask_state() == 0&&UserUtil.getCurrentUserType() == UserUtil.EMPLOYEE_TYPE){
                getTaskBtn.setVisibility(View.VISIBLE);
                getTaskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getTask();                    }
                });
            }
        } else {
            setButton();
        }
        initMenu();
    }

    /**
     * 领取任务
     */
    private void getTask() {
        Map<String,String> map = new HashMap<>();
        map.put("task_id",task.getTask_id()+"");
        map.put("employee_id",String.valueOf(((Employee)UserUtil.getCurrentUser()).getEmployee_id()));
        HttpRequestServer.create(this).doGetWithParams(GET_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,false)){
                    ToastUtil.getInstance().log("领取成功");
                    finish();
                }
            }
        });
    }

    private void setButton() {
        addTaskBtn.setVisibility(View.VISIBLE);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        Map<String,String> map = new HashMap<>();
        map.put("task_name",taskNameEt.getText().toString());
        map.put("task_customer", ((Custom)UserUtil.getCurrentUser()).getCustomer_id()+"");
        map.put("task_info",taskInfoEt.getText().toString());
        map.put("task_money",taskMoneyEt.getText().toString());
        map.put("task_phone",taskPeoplePhoneEt.getText().toString());
        map.put("task_people",taskPeopleEt.getText().toString());
        map.put("task_area",((Custom)UserUtil.getCurrentUser()).getArea_id()+"");
        HttpRequestServer.create(this).doGetWithParams(ADD_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,false)){
                    ToastUtil.getInstance().log("添加任务成功");
                    finish();
                }
            }
        });
    }

    /**
     * 数据展示
     */
    private void setData() {
        taskTimeEt.setText(task.getTask_time());
        taskTimeEt.setFocusable(false);
        taskNameEt.setText(task.getTask_name());
        taskNameEt.setFocusable(false);
        taskInfoEt.setText(task.getTask_info());
        taskInfoEt.setFocusable(false);
        taskMoneyEt.setText(task.getTask_money());
        taskMoneyEt.setFocusable(false);
        taskPeopleEt.setText(task.getTask_people());
        taskPeopleEt.setFocusable(false);
        taskPeoplePhoneEt.setText(task.getTask_phone());
        taskPeoplePhoneEt.setFocusable(false);
        taskStateNameEt.setText(task.getTask_state_name());
        taskStateNameEt.setFocusable(false);
    }

    /**
     * 初始化返回按钮
     */
    private void initMenu() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        focuceLl.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
