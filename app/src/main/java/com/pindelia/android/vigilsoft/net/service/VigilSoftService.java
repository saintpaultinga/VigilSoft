package com.pindelia.android.vigilsoft.net.service;

import com.google.gson.JsonElement;
import com.pindelia.android.vigilsoft.entity.Visitor;
import com.pindelia.android.vigilsoft.net.tools.ApiResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VigilSoftService {
    @POST("/visitors")
    Observable<JsonElement> createVisitor(@Body Visitor visitor);
}
