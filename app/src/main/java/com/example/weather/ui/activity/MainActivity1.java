package com.example.weather.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.ui.viewmodel.MainActivityViewModel;
import com.example.weather.utils.adapter.ForecastAdapter;
import com.example.weather.utils.model.CurrentTemperatureModel;
import com.example.weather.utils.model.ForeCastRecyclerModel;
import com.example.weather.utils.model.ForecastTemperatureModel;
import com.example.weather.utils.reterofit.RetrofitBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity1 extends AppCompatActivity {

    MainActivityViewModel mViewModel;
    ViewDataBinding mBinding;
    RecyclerView recyclerView;
    List<ForeCastRecyclerModel> finalList = new ArrayList<>();

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String APP_ID = "9b8cb8c7f11c077f8c4e217974d9ee40";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main1);
        mBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mBinding.setViewmodel(mViewModel);
        mBinding.setClickHandler(new ClickHandler());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        observeLiveData();
        getLastLocation();
        checkTimeOut();

    }

    public void observeLiveData()
    {
        mViewModel.forecastTemperatureModels.observe(this, forecastTemperatureModel -> {
            List<Integer> uniqueIndexList = new ArrayList<>();
            List<String> uniqueDateList = new ArrayList<>();
            List<String> serverDateList = new ArrayList<>();
            List<Integer> serverTemperatureList = new ArrayList<>();
            if(forecastTemperatureModel!=null )
            {
                Log.i("TAG_R","Once");
                for(int i= 0; i < forecastTemperatureModel.getList().size();i++)
                {
                    String[] splited = forecastTemperatureModel.getList().get(i).getDtTxt().split("\\s+");
                    serverDateList.add(splited[0]);
                    serverTemperatureList.add(convertIntoCelsius(forecastTemperatureModel.getList().get(i).getMain().getTemp()));

                }

                String previous = serverDateList.get(0);
                for(int i = 0; i < serverDateList.size(); i++)
                {
                    if(!previous.equals(serverDateList.get(i)))
                    {
                        uniqueIndexList.add(i);
                        uniqueDateList.add(serverDateList.get(i));
                        previous = serverDateList.get(i);

                    }
                }
                makeFinalList(uniqueIndexList, uniqueDateList, serverDateList, serverTemperatureList);
                mViewModel.setProgress(false);
                mViewModel.setError(false);

            }

        });
    }
    private void initializeRecycler()
    {
        recyclerView = mBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ForecastAdapter(finalList));
    }

    private void loadData(String cityname) {

        RetrofitBuilder.getInstance().getMyWeatherApi().loadCurrentTemperature(cityname, APP_ID).enqueue(new Callback<CurrentTemperatureModel>() {
            @Override
            public void onResponse(@NotNull Call<CurrentTemperatureModel> call, @NotNull Response<CurrentTemperatureModel> response) {

                try {
                    if (response.isSuccessful())
                    {
                        mBinding.setCurrentTemp(String.valueOf(convertIntoCelsius(response.body().getMain().getTemp())));

                    }
                }
                catch (Exception e)
                {
                    Log.i("TAG_CATCH","Exception : "+e.getMessage());

                }

            }

            @Override
            public void onFailure(@NotNull Call<CurrentTemperatureModel> call, @NotNull Throwable t) {
                Log.i("TAG_1","onFailure : "+ t.getLocalizedMessage());
                mViewModel.setProgress(false);
                mViewModel.setError(true);

            }
        });
    }
    private void loadForecast(String cityname) {
        RetrofitBuilder.getInstance().getMyWeatherApi().loadForecast(cityname , APP_ID).enqueue(new Callback<ForecastTemperatureModel>() {
            @Override
            public void onResponse(@NotNull Call<ForecastTemperatureModel> call, @NotNull Response<ForecastTemperatureModel> response) {
                try {
                    if (response.body() != null) {

                        mViewModel.setForecastTemperatureModels(response.body());
                    }
                }
                catch (Exception e)
                {
                    Log.i("TAG_CATCH","Exception : "+e.getMessage());

                }


            }

            @Override
            public void onFailure(@NotNull Call<ForecastTemperatureModel> call, @NotNull Throwable t) {
                Log.i("TAG_2","onFailure : "+t.getMessage());
                mViewModel.setProgress(false);
                mViewModel.setError(true);
            }
        });
    }

    private void makeFinalList(List<Integer> uniqueIndexList, List<String> uniqueDateList, List<String> dtList, List<Integer> tempList) {

        List<List<Integer>> temperatureSubSetsList = new ArrayList<>();

        for(int i=0; i< uniqueIndexList.size(); i++)
        {
            if(i==uniqueIndexList.size()-1)
            {
                temperatureSubSetsList.add(tempList.subList(uniqueIndexList.get(i),dtList.size()));
            }
            else
            {
                temperatureSubSetsList.add(tempList.subList(uniqueIndexList.get(i),uniqueIndexList.get(i+1)));
            }

        }

        for(int i = 0; i< temperatureSubSetsList.size();i++)
        {
            finalList.add(new ForeCastRecyclerModel(uniqueDateList.get(i),(int) calculateAverage(temperatureSubSetsList.get(i))));
        }


        initializeRecycler();
        Animation moveUp = AnimationUtils.loadAnimation(this, R.anim.move_up);
        mBinding.cardView.setVisibility(View.VISIBLE);
        mBinding.cardView.startAnimation(moveUp);

    }


    private double calculateAverage(List<Integer> tempList) {
        Integer sum = 0;
        if(!tempList.isEmpty()) {
            for (Integer temp : tempList) {
                sum += temp;
            }
            return sum.doubleValue() / tempList.size();
        }
        return sum;
    }

    public static int convertIntoCelsius(Double temp)
    {
        return (int) (temp-273.15);
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    try {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            getLocaleFromGeo(location.getLatitude(), location.getLongitude());
                        }
                    }
                    catch (Exception e)
                    {
                        mViewModel.setError(true);
                        mViewModel.setProgress(false);
                        Toast.makeText(MainActivity1.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            requestPermissions();
        }


    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            getLocaleFromGeo(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        }
    };
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }


    public void getLocaleFromGeo(double latitude, double longitude)
    {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;

            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
            {
                loadData(addresses.get(0).getLocality());
                loadForecast(addresses.get(0).getLocality());
                mBinding.setCityName(addresses.get(0).getLocality());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class ClickHandler
    {
        private MainActivityViewModel mViewModel;

        public void onRetryClick(View view)
        {
            mViewModel.setProgress(true);

            new Handler().postDelayed(() -> {
                getLastLocation();
                checkTimeOut();
            }, 500);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mViewModel.currentTemperatureModel==null)
        {
            getLastLocation();

        }

    }

    private void checkTimeOut() {

        new Handler().postDelayed(() -> {
            if(mViewModel.getProgress().getValue())
            {
                mViewModel.setError(true);
                mViewModel.setProgress(false);
            }

        }, 8000);

    }
}