package com.junlong0716.rxretrofit;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by ${巴黎没有摩天轮Li} on 2017/9/19.
 */

public interface Api {
    String baseUrl = "http://order.htxcsoft.com/";

    @GET("testForAz2.do")
    Observable<ResponseBody> getFoodType(@Query("page") String page);

    @Multipart
    @POST("testUploadPic.do")
    Flowable<ResultModel<String>> upload(@Part MultipartBody.Part file);


    @Multipart
    @POST("testUploadPic1.do")
    Flowable<ResultModel<String>> uploads(@Part ArrayList<MultipartBody.Part> files);

    @FormUrlEncoded
    @POST("testForAz1.do")
    Observable<ResponseBody> getPostData(@Field("page") String page);

    @Streaming
    @GET
    Flowable<ResponseBody> startDownLoad(@Url String fileUrl);

}
