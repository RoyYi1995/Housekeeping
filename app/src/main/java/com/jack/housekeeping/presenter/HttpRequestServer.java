package com.jack.housekeeping.presenter;

import android.content.Context;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HttpRequestServer {
    private static Context mContext;
    //基础请求地址
    private static String base_url;
    private static OkHttpClient mClient;
    private static HttpAPI mHttpAPI;

    public static final String TAG = "httpRequest";
    private HttpRequestServer(Context context) {
        this(context, null, null);
    }

    private HttpRequestServer(Context context, String base_url, OkHttpClient client) {
        mClient = client;
        this.mContext = context;
        this.base_url = base_url;
        buildServer();
    }

    private void buildServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpAPI.BASE_URL)
                .client(mClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mHttpAPI = retrofit.create(HttpAPI.class);
    }


    private static class HttpRequestServerHolder {
        public static OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LogIntercept())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        public static HttpRequestServer server = new HttpRequestServer(mContext, base_url, client);
    }

    public static HttpRequestServer create(Context context, String base_url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(mClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mHttpAPI = retrofit.create(HttpAPI.class);

        return null;
    }

    public static HttpRequestServer create(Context context) {
        if (context == null)
            mContext = context;
        return HttpRequestServerHolder.server;
    }

    /**
     * 使用json数据进行post请求
     *
     * @param path     基于baseUral 后的地址
     * @param json     json字符串
     * @param callBack Subscriber的回调
     */
    public void doPostWithJson(String path, String json, Subscriber<ResponseBody> callBack) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        mHttpAPI.questJsonValue(path, body,LogIntercept.session)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

    /**
     * 使用参数的形式进行post提交
     * ep:username=abc&age=14
     * Map<String,String> params params.put("username","abc") params.put("age","14")
     *
     * @param path     基于baseUral 后的地址
     * @param params
     * @param callBack
     */
    public void doPostWithParam(String path, Map params, Subscriber<ResponseBody> callBack) {
        Log.i(TAG, "doPostWithParam: "+params);
        mHttpAPI.doPost3(path, params,LogIntercept.session)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

    /**
     * 使用参数的形式进行post提交
     * ep:username=abc&age=14
     * Map<String,String> params params.put("username","abc") params.put("age","14")
     *
     * @param path     基于baseUral 后的地址
     * @param
     * @param callBack
     */
    public void doPostWithParamForUser(String path, int uid, String token, Subscriber<ResponseBody> callBack) {
        mHttpAPI.doPost2(path, uid,token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

    /**
     * 文件上传
     *
     * @param path      基于baseUral 后的地址
     * @param callBack
     */
    public void uploadFile(String path, RequestBody multipart, Subscriber<ResponseBody> callBack) {
        mHttpAPI.upLoadFile(path,multipart,LogIntercept.session)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 不需要传参的post
     * @param path
     * @param callBack
     */
    public void doPost(String path, Subscriber<ResponseBody> callBack){
        mHttpAPI.doPostNoParams(path)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void doGet(String path, Map<String,String> params, Subscriber<ResponseBody> callBack){
        mHttpAPI.getRequest(path,params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    public void doPostImage(String path, RequestBody requestBody, MultipartBody.Part file, Subscriber<ResponseBody> callBack){
        mHttpAPI.upLoadImage(path,requestBody,file)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

    public void doLogin(String path, Map params, Subscriber<ResponseBody> callBack) {
        mHttpAPI.doPost3(path, params,LogIntercept.session)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);

    }

}
