package com.utku.invoice_upload_system.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpRequests {
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public String getReq(String url, String type, String value){
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

        httpBuilder.addQueryParameter("Type",type);
        if(type.equals("name")){
            httpBuilder.addQueryParameter("Name", value);
        }else{
            httpBuilder.addQueryParameter("Serial", value);
        }

        Request request = new Request.Builder().url(httpBuilder.build()).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "İşlem sırasında hata oluştu!";
    }

    public String postReq(String url, String type, String value){
        String ContentType;
        if(type.equals("xml")){
            ContentType = "application/xml";
        }else{
            ContentType = "application/json";
        }
        RequestBody body = RequestBody.create(
                MediaType.parse(ContentType), value);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Type", type)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "İşlem sırasında hata oluştu!";
    }
}
