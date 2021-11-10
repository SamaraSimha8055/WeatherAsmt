package com.example.weather.utils.reterofit;


import com.example.weather.utils.model.CurrentTemperatureModel;
import com.example.weather.utils.model.ForecastTemperatureModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherApi {

    String BASE_URL = "https://api.openweathermap.org/data/2.5/";


    @GET("weather")
    Call<CurrentTemperatureModel> loadCurrentTemperature(@Query("q") String cityname, @Query("APPID") String appid);

    @GET("forecast")
    Call<ForecastTemperatureModel> loadForecast(@Query("q") String cityname, @Query("APPID") String appid);

}
