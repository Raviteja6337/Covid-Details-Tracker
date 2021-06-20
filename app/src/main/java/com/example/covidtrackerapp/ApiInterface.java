package com.example.covidtrackerapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    String base_url = "https://corona.lmao.ninja/v2/";

    @GET("countries")
        Call<List<CovidDetailsDAO>> getCountiresData();
}
