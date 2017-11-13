package com.jack.housekeeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jack.housekeeping.R;
import com.jack.housekeeping.bean.Custom;
import com.jack.housekeeping.presenter.HttpRequestServer;
import com.jack.housekeeping.utils.ResponseUtil;
import com.jack.housekeeping.utils.UserUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class LoginActivity extends AppCompatActivity {
    private final String CUSTOMER_LOGIN_URL = "/customer/login";
    private final String EMPLOYEE_LOGIN_URL = "/employee/login";
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
                doSignUp();
                break;
        }
    }

    /**
     * 注册
     */
    private void doSignUp() {
        Intent intent = new Intent(this, CustomSignUpActivity.class);
        if (customerRb.isChecked()) {
            intent.putExtra("userType", "custom");
        } else {
            intent.putExtra("userType", "employee");
        }
        startActivity(intent);
    }

    /**
     * 登录
     */
    private void doLogin() {
        String userName = phoneEt.getText().toString();
        String password = passwordEt.getText().toString();
        if (customerRb.isChecked()){
            customLogin(userName,password);
        }else{
            employeeLogin(userName,password);
        }

    }

    /**
     * 员工登录
     * @param userName
     * @param password
     */
    private void employeeLogin(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("employee_id", userName);
        map.put("employee_password", password);
        HttpRequestServer.create(this).doGetWithParams(EMPLOYEE_LOGIN_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,true)) {
                    // TODO: 2017/11/13 需要修改 
                    Custom user = (Custom) ResponseUtil.getByType(Custom.class);
                    UserUtil.setCustomType(UserUtil.CUSTOM_TYPE);
                    UserUtil.setUser(user);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        });
    }

    /**
     * 客户登录
     * @param userName
     * @param password
     */
    private void customLogin(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", password);
        HttpRequestServer.create(this).doGetWithParams(CUSTOMER_LOGIN_URL, map, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                KLog.i(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (ResponseUtil.verify(responseBody,true)) {
                    Custom user = (Custom) ResponseUtil.getByType(Custom.class);
                    UserUtil.setCustomType(UserUtil.CUSTOM_TYPE);
                    UserUtil.setUser(user);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        });
    }
}
