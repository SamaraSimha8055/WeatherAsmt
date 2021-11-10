package com.example.weather.utils.reterofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static RetrofitBuilder instance = null;
    private final WeatherApi myWeatherApi;

    private RetrofitBuilder() {

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder().baseUrl(WeatherApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myWeatherApi = retrofit.create(WeatherApi.class);

    }

    public static synchronized RetrofitBuilder getInstance() {

        if (instance == null) {
            instance = new RetrofitBuilder();
        }

        return instance;
    }

    public WeatherApi getMyWeatherApi() {

        return myWeatherApi;
    }
}

