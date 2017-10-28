package com.jack.housekeeping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Area;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class CustomSignUpActivity extends AppCompatActivity {

    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.area_et)
    EditText areaEt;

    private final String SIGN_UP_URL = "/customer/signUp";
    private final String GETAREA_URL = "/common/areaInfo";
    private ArrayList<Area> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acitity);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化地区信息
     */
    private void initData() {
        HttpRequestServer.create(this).doGet(GETAREA_URL, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,true)){
                    arrayList = (ArrayList<Area>) ResponseUtil.getByType(new TypeToken<ArrayList<Area>>(){}.getType());
                    KLog.i(arrayList.size());
                }
            }
        });
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        signUp();
    }

    /**
     * 注册
     */
    private void signUp() {
        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String area = areaEt.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("customer_name", name);
        map.put("customer_password", password);
        map.put("customer_phone", phone);
        map.put("customer_area", area);
        HttpRequestServer.create(this).doGetWithParams(SIGN_UP_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody, false)) {
                    finish();
                }
            }
        });
    }
}
