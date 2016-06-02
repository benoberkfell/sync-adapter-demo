package com.example.syncdemo.rest;

import com.example.syncdemo.models.MessageListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessageService {
    @GET("/messages")
    Call<MessageListResponse> recentMessages(@Query("since") long timestamp);
}
