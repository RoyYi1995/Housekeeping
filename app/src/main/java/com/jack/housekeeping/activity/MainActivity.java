package com.jack.housekeeping.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Custom;
import com.jack.housekeeping.bean.Employee;
import com.jack.housekeeping.bean.Task;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.jack.housekeeping.utils.ToastUtil;
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
import okhttp3.ResponseBody;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.task_rv)
    RecyclerView taskRv;
    @BindView(R.id.null_rl)
    RelativeLayout nullRl;
    private final String MYTASK_URL = "/customer/myTask";
    private final String AREATASK_URL = "/employee/areaTask";

    private final int ADD_TASK_CODE = 101;
    private final int GET_TASK_CODE = 102;

    private ArrayList<Task> tasks;
    private boolean isExit = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

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
        if (tasks == null || tasks.size() == 0) {
            nullRl.setVisibility(View.VISIBLE);
            return;
        }
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
            fab.setBackgroundResource(R.drawable.add);
        } else {
            toolbar.setSubtitle(((Employee) UserUtil.getCurrentUser()).getEmployee_name());
            fab.setBackgroundResource(R.drawable.my_task);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, EmploeeTaskActivity.class));
                }
            });
        }
        setSupportActionBar(toolbar);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (UserUtil.getCurrentUserType() == UserUtil.CUSTOM_TYPE)
            initCustomData();
        else
            initEmployeeData();
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
   *   BACK键退出
   * */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.getInstance().log("再次点击返回键退出");
            Message message = Message.obtain();
            message.what = 4;
            mHandler.sendMessageDelayed(message, 2000);
        } else {
            finish();
        }
    }

}
