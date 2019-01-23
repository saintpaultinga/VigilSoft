package com.pindelia.android.vigilsoft.net.service;

import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.net.tools.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VigilSoftService {
    @POST("/visitors")
    Call<ApiResponse> createVisitor(@Body Visitor visitor);
}
