package com.jack.housekeeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Custom;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.task_rv)
    RecyclerView taskRv;

    private final String MYTASK_URL = "/customer/myTask";
    private ArrayList<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String customer_id = String.valueOf(((Custom) UserUtil.getCurrentUser()).getCustomer_id());
        Map<String,String> requestValues = new HashMap<>();
        requestValues.put("customer_id",customer_id);
        HttpRequestServer.create(this).doGetWithParams(MYTASK_URL, requestValues, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,true)){
                    tasks = (ArrayList<Task>) ResponseUtil.getByType(new TypeToken<ArrayList<Task>>(){
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
        CommonAdapter<Task> taskCommonAdapter = new CommonAdapter<Task>(this,R.layout.view_tast_show,tasks) {
            @Override
            protected void convert(ViewHolder holder, Task task, int position) {
                holder.setText(R.id.task_name_tv,task.getTask_name());
                holder.setText(R.id.task_info_tv,task.getTask_info());
                holder.setText(R.id.task_money_tv,"￥"+task.getTask_money());
                holder.setText(R.id.task_state_name_tv,task.getTask_state_name());
            }
        };
        taskCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // TODO: 2017/11/6 跳转到任务详情页面
                Intent intent = new Intent();
                intent.putExtra("task",tasks.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        taskRv.setAdapter(taskCommonAdapter);
    }

    /**
     *  初始化视图
     */
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitle(((Custom) UserUtil.getCurrentUser()).getCustomer_name());
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent());
            }
        });
    }

}
