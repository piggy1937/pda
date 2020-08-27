package com.tech.libnetwork;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PostRequest<T> extends Request<T, PostRequest> {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public PostRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        //post请求表单提交
       // FormBody.Builder bodyBuilder = new FormBody.Builder();
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
////            bodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
////        }
        String json = JSONObject.toJSONString(params);
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = builder.url(mUrl).post(body).build();
        return request;
    }
}
