package com.example.weather.utils.tools;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapterUtil {

    @BindingAdapter("app:currentTemp")
    public static void loadCurrentTemp(TextView textView, String temp)
    {
        textView.setText(temp+"\u00B0");
    }

    @BindingAdapter("app:weekdate")
    public static void setWeekDate(TextView textView, Integer date)
    {

        switch (date)
        {
            case 1:
                textView.setText("Sunday");
                break;
            case 2:
                textView.setText("Monday");
                break;
            case 3:
                textView.setText("Tuesday");

                break;
            case 4:
                textView.setText("Wednesday");

                break;
            case 5:
                textView.setText("Thursday");

                break;
            case 6:
                textView.setText("Friday");

                break;
            case 7:
                textView.setText("Saturday");

                break;

        }
    }

    @BindingAdapter("app:averageTemperature")
    public static void setAverageTemperature(TextView textView, Integer average)
    {
        textView.setText(average+" C ");
    }
}
