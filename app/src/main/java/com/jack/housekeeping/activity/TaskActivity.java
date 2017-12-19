package com.jack.housekeeping.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Custom;
import com.jack.housekeeping.bean.Employee;
import com.jack.housekeeping.bean.Task;
import com.jack.housekeeping.bean.TaskOtherInfo;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.jack.housekeeping.utils.ToastUtil;
import com.jack.housekeeping.utils.UserUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class TaskActivity extends AppCompatActivity {

    private final String ADD_TASK_URL = "/customer/addTask";
    private final String GET_TASK_URL = "/employee/employeeToTask";
    private final String DONE_TASK_URL = "/employee/complateTask";
    private final String EVA_TASK_URL = "/customer//addTaskEVA";
    private final String GET_TASK_OTHER_URL = "/customer/myTaskInfo";
    private final String GET_EVA_URL = "/employee/myTaskEVA";
    public final int NOTACCEPTED = 1;
    public final int ACCEPTED = 2;
    public final int COMPLETED = 3;
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
    @BindView(R.id.employee_tv)
    TextView employeeTv;
    @BindView(R.id.employee_ll)
    LinearLayout employeeLl;
    @BindView(R.id.eva_rv)
    RecyclerView evaRv;
    private Task task;
    private TaskOtherInfo otherInfo;
    private ArrayList<String> evas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        initview();
        initMenu();
    }

    /**
     * 初始化视图
     */
    private void initview() {
        task = (Task) getIntent().getSerializableExtra("task");
        KLog.i(task.getTask_state());
        if (task != null) {
            setData();
            if (task.getTask_state() == COMPLETED && UserUtil.getCurrentUserType() == UserUtil.EMPLOYEE_TYPE) {
                getEvaData();
            }
            if (task.getTask_state() != NOTACCEPTED && UserUtil.getCurrentUserType() == UserUtil.CUSTOM_TYPE) {
                KLog.i("任务已接收，用户");
                setOtherInfo();
            }
            if (task.getTask_state() == NOTACCEPTED && UserUtil.getCurrentUserType() == UserUtil.EMPLOYEE_TYPE) {
                getTaskBtn.setVisibility(View.VISIBLE);
                getTaskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getTask();
                    }
                });
            } else if (task.getTask_state() == ACCEPTED && UserUtil.getCurrentUserType() == UserUtil.EMPLOYEE_TYPE) {
                getTaskBtn.setText("完成任务");
                getTaskBtn.setVisibility(View.VISIBLE);
                getTaskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(TaskActivity.this)
                                .setTitle("提示")
                                .setMessage("是否完成任务?")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        doneTask();
                                    }
                                })
                                .create().show();
                    }
                });
            } else if (task.getTask_state() == COMPLETED && UserUtil.getCurrentUserType() == UserUtil.CUSTOM_TYPE) {
                getTaskBtn.setText("评价任务");
                getTaskBtn.setVisibility(View.VISIBLE);
                final LinearLayout layout = new LinearLayout(this);
                final EditText text = new EditText(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                text.setLayoutParams(params);
                layout.addView(text);
                getTaskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(TaskActivity.this)
                                .setTitle("提示")
                                .setView(layout)
                                .setNegativeButton("取消", null)
                                .setPositiveButton("评价", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        evaTask(text.getText().toString());
                                    }
                                })
                                .create().show();
                    }
                });
            }
        } else {
            setButton();
        }
    }

    /**
     * 获取评论列表
     */
    private void getEvaData() {
        Map<String, String> map = new HashMap<>();
        map.put("task_id", task.getTask_id() + "");
        HttpRequestServer.create(this).doGetWithParams(GET_EVA_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,true)){

                }
            }
        });
    }

    /**
     * 获取额外信息
     */
    private void setOtherInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("task_id", task.getTask_id() + "");
        HttpRequestServer.create(this).doGetWithParams(GET_TASK_OTHER_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, true)) {
                    otherInfo = (TaskOtherInfo) ResponseUtil.getByType(TaskOtherInfo.class);
                    otherInfo.setTask(task);
                    setOtherView();
                }
            }
        });
    }

    /**
     * 设置其他信息
     */
    private void setOtherView() {
        employeeLl.setVisibility(View.VISIBLE);
        employeeTv.setText(otherInfo.getEmployee_name());
    }

    /**
     * 添加任务评价
     *
     * @param text
     */
    private void evaTask(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("task_id", task.getTask_id() + "");
        map.put("customer_id", task.getTask_customer());
        map.put("employee_id", otherInfo.getEmployee_id());
        map.put("evaluation_info", text);
        HttpRequestServer.create(this).doGetWithParams(ADD_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, false)) {
                    ToastUtil.getInstance().log("添加评价成功");
                    finish();
                }
            }
        });
    }

    /**
     * 完成任务
     */
    private void doneTask() {
        Map<String, String> map = new HashMap<>();
        map.put("task_id", task.getTask_id() + "");
        HttpRequestServer.create(this).doGetWithParams(DONE_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, false)) {
                    ToastUtil.getInstance().log("完成任务");
                    finish();
                }
            }
        });
    }

    /**
     * 领取任务
     */
    private void getTask() {
        Map<String, String> map = new HashMap<>();
        map.put("task_id", task.getTask_id() + "");
        map.put("employee_id", String.valueOf(((Employee) UserUtil.getCurrentUser()).getEmployee_id()));
        HttpRequestServer.create(this).doGetWithParams(GET_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, false)) {
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
        Map<String, String> map = new HashMap<>();
        map.put("task_name", taskNameEt.getText().toString());
        map.put("task_customer", ((Custom) UserUtil.getCurrentUser()).getCustomer_id() + "");
        map.put("task_info", taskInfoEt.getText().toString());
        map.put("task_money", taskMoneyEt.getText().toString());
        map.put("task_phone", taskPeoplePhoneEt.getText().toString());
        map.put("task_people", taskPeopleEt.getText().toString());
        map.put("task_area", ((Custom) UserUtil.getCurrentUser()).getArea_id() + "");
        HttpRequestServer.create(this).doGetWithParams(ADD_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, false)) {
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
