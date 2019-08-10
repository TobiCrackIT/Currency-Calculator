package com.example.currcalc.model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    String BASE_URL ="http://data.fixer.io/api/";




    @GET("latest?access_key=704440b52b7318a192bdaf1e460d2d4a&format=1")
    Call<Object> getCurr();
}
