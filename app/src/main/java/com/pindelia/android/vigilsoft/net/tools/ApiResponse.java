package com.pindelia.android.vigilsoft.net.tools;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;

public class ApiResponse {

    public final Status status;
    @Nullable
    public final JsonElement data;
    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable JsonElement data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(Status.LOADING, null, null);
    }

    public static ApiResponse success(@Nullable JsonElement data) {
        return new ApiResponse(Status.SUCCESS, data, null);
    }

    public static ApiResponse error(@Nullable Throwable error) {
        return new ApiResponse(Status.ERROR, null, error);
    }

}
