package com.example.weather.utils.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.databinding.ForecastItemBinding;
import com.example.weather.utils.model.ForeCastRecyclerModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    List<ForeCastRecyclerModel> finalList;

    public ForecastAdapter(List<ForeCastRecyclerModel> finalList) {
        this.finalList = finalList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.forecast_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

        try {
            holder.itemBinding.setWeekDate(convertToWeekDate(finalList.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemBinding.setAverageTemp(finalList.get(position).getTemp());

    }

    @Override
    public int getItemCount() {
        return finalList.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        ForecastItemBinding itemBinding;

        public ForecastViewHolder(@NonNull ForecastItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }


    public static int convertToWeekDate(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        assert date1 != null;
        cal.setTime(date1);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

}
