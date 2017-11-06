package com.jack.housekeeping.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Task;

public class TaskActivity extends AppCompatActivity {

    private Task task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        task = (Task) getIntent().getSerializableExtra("task");
        if (task != null){
            setData();
        }
        initMenu();
    }

    /**
     * 数据展示
     */
    private void setData() {

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
