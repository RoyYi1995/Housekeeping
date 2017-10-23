package com.jack.housekeeping.presenter;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class LogIntercept implements Interceptor {

    public static final String TAG = "Interceptor";
    public static String session = "";

    public LogIntercept() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        long t1 = System.nanoTime();
        Log.i(TAG, String.format("Sending request is %n %s", request.toString())+request.header("Cookie"));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        Headers headers = response.headers();
        List<String> cookies = headers.values("Set-Cookie");
        if (cookies.size()!=0){
            session = cookies.get(0);
            Log.i(TAG, "intercept: "+session);
        }
        return response;
    }
}
