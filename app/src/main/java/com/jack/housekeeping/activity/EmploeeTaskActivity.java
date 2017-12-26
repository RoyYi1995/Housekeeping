package com.jack.housekeeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Employee;
import com.jack.housekeeping.bean.Task;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.jack.housekeeping.utils.UserUtil;
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

public class EmploeeTaskActivity extends AppCompatActivity {

    private final String EMPLOYEE_TASK_URL = "/employee/myAreaTask";
    private final int EMPLOYEE_TASK = 104;
    @BindView(R.id.employee_rl)
    RecyclerView employeeRl;
    @BindView(R.id.null_task_rl)
    RelativeLayout nullTaskRl;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploee_task);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("已领取任务");
        initMenu();
        initData();
    }

    /**
     * 获取员工已经接收的任务
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("employee_id", ((Employee) UserUtil.getCurrentUser()).getEmployee_id());
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

    /**
     * 初始化列表
     */
    private void initRv() {
        employeeRl.setLayoutManager(new LinearLayoutManager(this));
        if (tasks == null || tasks.size() == 0){
            nullTaskRl.setVisibility(View.VISIBLE);
            return;
        }
        else
            nullTaskRl.setVisibility(View.GONE);
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
                Intent intent = new Intent(EmploeeTaskActivity.this, TaskActivity.class);
                intent.putExtra("task", tasks.get(position));
                startActivityForResult(intent, EMPLOYEE_TASK);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        employeeRl.setAdapter(taskCommonAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMPLOYEE_TASK) {
            initData();
        }
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
