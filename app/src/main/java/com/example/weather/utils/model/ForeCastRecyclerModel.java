package com.example.weather.utils.model;

public class ForeCastRecyclerModel {

    String date;
    int temp;

    public ForeCastRecyclerModel(String date, int temp) {
        this.date = date;
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
