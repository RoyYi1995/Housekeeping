package com.jack.housekeeping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

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

public class LoginActivity extends AppCompatActivity {
    private final String LOGIN_URL = "/customer/login";
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.customer_rb)
    RadioButton customerRb;
    @BindView(R.id.employee_rb)
    RadioButton employeeRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_btn, R.id.signUp_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                doLogin();
                break;
            case R.id.signUp_btn:
                break;
        }
    }

    private void doLogin() {
        String userName = phoneEt.getText().toString();
        String password = passwordEt.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", password);
        HttpRequestServer.create(this).doGet(LOGIN_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                KLog.i(e);
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
