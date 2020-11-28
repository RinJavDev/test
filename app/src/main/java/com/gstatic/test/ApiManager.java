package com.gstatic.test;



import com.gstatic.test.pojo.LocationDataResult;
import com.gstatic.test.pojo.LoginResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiManager {


    String BASE_URL = "http://www.alarstudios.com";

    @GET("/test/auth.cgi")
    Observable<LoginResult> login(@Query("username") String action, @Query("password") String envelopeId);

    @GET("/test/data.cgi ")
    Observable<LocationDataResult> loadLocationData(@Query("code") String code, @Query("p") int page);
}
