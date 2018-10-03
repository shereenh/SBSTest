package com.shereen.sbstest.rest;



import com.shereen.sbstest.gson.TopLevel;
import retrofit2.http.GET;


public interface RestService {

    @GET("api/categories")
    io.reactivex.Observable<TopLevel> getSBSResult();

}

