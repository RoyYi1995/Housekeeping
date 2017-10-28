package com.jack.housekeeping.presenter;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface HttpAPI {

    //基础地址
    String BASE_URL = "http://39.108.120.110:8080";

    /**
     * 提交json数据
     *
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("{path}")
    Observable<ResponseBody> questJsonValue(@Path(value = "path", encoded = true) String path, @Body RequestBody requestBody, @Header("Cookie") String cookie);

    /**
     * 以对象的形式进行返回
     *
     * @param path   请求地址
     * @param params 参数集
     * @return
     */
    @GET("{path}")
    Observable<ResponseBody> getRequest(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, String> params);

    /**
     * 以post 表单的形式进行传输
     *
     * @param path
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> doPost(@Path(value = "path", encoded = true) String path,
                                    @FieldMap Map<String, String> params);

    /**
     * 以post 表单的形式进行传输
     *
     * @param path
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> doPost2(@Path(value = "path", encoded = true) String path, @Field("uid") int uid,
                                     @Field("token") String token);

    /**
     * 以post 表单的形式进行传输
     *
     * @param path
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> doPost3(@Path(value = "path", encoded = true) String path,
                                     @FieldMap Map<String, String> params, @Header("Cookie") String cookie);

    /**
     * 上传文件
     *
     * @return
     */
    @POST("{path}")
    Observable<ResponseBody> upLoadFile(@Path(value = "path", encoded = true) String path, @Body RequestBody file, @Header("Cookie") String cookie);

    @POST("{path}")
    Observable<ResponseBody> doPostNoParams(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<ResponseBody> doGetNoParams(@Path(value = "path", encoded = true) String path);

    @Multipart
    @POST("{path}")
    Observable<ResponseBody> upLoadImage(@Path(value = "path", encoded = true) String path, @Part RequestBody body, @Part MultipartBody.Part file);
}
