package com.example.weather.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.weather.utils.model.CurrentTemperatureModel;
import com.example.weather.utils.model.ForecastTemperatureModel;


public class MainActivityViewModel extends AndroidViewModel {

    public MutableLiveData<CurrentTemperatureModel> currentTemperatureModel;
    public MutableLiveData<ForecastTemperatureModel> forecastTemperatureModels;
    public MutableLiveData<Boolean> progress;
    public MutableLiveData<Boolean> error;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {

        currentTemperatureModel = new MutableLiveData<>();
        forecastTemperatureModels = new MutableLiveData<>();
        progress = new MutableLiveData<>(true);
        error = new MutableLiveData<>(true);
    }


    public MutableLiveData<CurrentTemperatureModel> getCurrentTemperatureModel() {
        return currentTemperatureModel;
    }

    public void setCurrentTemperatureModel(CurrentTemperatureModel currentTemperatureModel) {
        this.currentTemperatureModel.setValue(currentTemperatureModel);
    }


    public MutableLiveData<ForecastTemperatureModel> getForecastTemperatureModels() {
        return forecastTemperatureModels;
    }

    public void setForecastTemperatureModels(ForecastTemperatureModel forecastTemperatureModels) {
        this.forecastTemperatureModels.setValue(forecastTemperatureModels);
    }

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }

    public void setProgress(Boolean progress) {
        this.progress.setValue(progress);
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error.setValue(error);
    }

}
