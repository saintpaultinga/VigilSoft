package com.pindelia.android.vigilsoft.net.service;


import com.google.gson.JsonElement;
import com.pindelia.android.vigilsoft.net.tools.Urls;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthenticationService {
    @FormUrlEncoded
    @POST(Urls.LOGIN_APP_ID_URL)
    Observable<JsonElement> login(@Field("mobile") String mobileNumber, @Field("password") String password);
}
