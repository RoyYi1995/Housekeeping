package com.jack.housekeeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Custom;
import com.jack.housekeeping.bean.Employee;
import com.jack.housekeeping.bean.Task;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.jack.housekeeping.utils.UserUtil;
import com.socks.library.KLog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.task_rv)
    RecyclerView taskRv;

    private final String MYTASK_URL = "/customer/myTask";
    private final String AREATASK_URL = "/employee/areaTask";
    private final String EMPLOYEE_TASK_URL = "/employee/myAreaTask";
    private final int ADD_TASK_CODE = 101;
    private final int GET_TASK_CODE = 102;
    private ArrayList<Task> tasks;
    private int taskPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (UserUtil.getCurrentUserType() == UserUtil.CUSTOM_TYPE) {
            initCustomData();
        } else {
            initEmployeeData();
        }
        initView();
    }

    /**
     * 初始化员工数据
     */
    private void initEmployeeData() {
        String employee_area_id = String.valueOf(((Employee) UserUtil.getCurrentUser()).getEmployee_area());
        KLog.i("客户id：" + employee_area_id);
        Map<String, String> map = new HashMap<>();
        map.put("employee_area", employee_area_id);
        HttpRequestServer.create(this).doGetWithParams(AREATASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, true)) {
                    tasks = (ArrayList<Task>) ResponseUtil.getByType(new TypeToken<ArrayList<Task>>() {
                    }.getType());
                    if (tasks == null) return;
                    initRv();
                }
            }
        });
    }

    /**
     * 初始化客户数据
     */
    private void initCustomData() {
        String customer_id = String.valueOf(((Custom) UserUtil.getCurrentUser()).getCustomer_id());
        Map<String, String> requestValues = new HashMap<>();
        requestValues.put("customer_id", customer_id);
        HttpRequestServer.create(this).doGetWithParams(MYTASK_URL, requestValues, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, true)) {
                    tasks = (ArrayList<Task>) ResponseUtil.getByType(new TypeToken<ArrayList<Task>>() {
                    }.getType());
                    if (tasks == null) return;
                    initRv();
                }
            }
        });
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        taskRv.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<Task> taskCommonAdapter = new CommonAdapter<Task>(this, R.layout.view_tast_show, tasks) {
            @Override
            protected void convert(ViewHolder holder, Task task, int position) {
                holder.setText(R.id.task_name_tv, task.getTask_name());
                holder.setText(R.id.task_info_tv, task.getTask_info());
                holder.setText(R.id.task_money_tv, "￥" + task.getTask_money());
                holder.setText(R.id.task_state_name_tv, task.getTask_state_name());
            }
        };
        taskCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra("task", tasks.get(position));
                taskPosition = position;
                startActivityForResult(intent, GET_TASK_CODE);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        taskRv.setAdapter(taskCommonAdapter);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        FloatingActionButton fab = findViewById(R.id.fab);
        if (UserUtil.getCurrentUserType() == UserUtil.CUSTOM_TYPE) {
            toolbar.setSubtitle(((Custom) UserUtil.getCurrentUser()).getCustomer_name());
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(MainActivity.this, TaskActivity.class), ADD_TASK_CODE);
                }
            });
            fab.setBackgroundDrawable(getResources().getDrawable(R.drawable.add));
        } else {
            toolbar.setSubtitle(((Employee) UserUtil.getCurrentUser()).getEmployee_name());
            fab.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_task));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getEmployeeTask(((Employee) UserUtil.getCurrentUser()).getEmployee_id());
                }
            });
        }
        setSupportActionBar(toolbar);

    }

    /**
     * 获取员工已经接收的任务
     *
     * @param employee_id
     */
    private void getEmployeeTask(String employee_id) {
        Map<String, String> map = new HashMap<>();
        map.put("employee_id", employee_id);
        HttpRequestServer.create(this).doGetWithParams(EMPLOYEE_TASK_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, true)) {
                    tasks = (ArrayList<Task>) ResponseUtil.getByType(new TypeToken<ArrayList<Task>>() {
                    }.getType());
                    if (tasks == null) return;
                    initRv();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (UserUtil.getCurrentUserType() == UserUtil.CUSTOM_TYPE)
            initCustomData();
        else
            initEmployeeData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.BUTTON_BACK) {

        }
        return super.onTouchEvent(event);
    }
}
