package com.jack.housekeeping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.jack.housekeeping.R;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class SignUpAcitity extends AppCompatActivity {

    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.area_et)
    EditText areaEt;

    private static final String SIGN_UP_URL = "/customer/signUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acitity);
        ButterKnife.bind(this);
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
        Map<String,String> map = new HashMap<>();
        map.put("username",name);
        map.put("password",password);
        map.put("phonenumber",phone);
        map.put("area",area);
        HttpRequestServer.create(this).doGet(SIGN_UP_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    KLog.i(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
