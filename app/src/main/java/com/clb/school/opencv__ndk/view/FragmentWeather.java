package com.clb.school.opencv__ndk.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.clb.school.opencv__ndk.R;
import com.clb.school.opencv__ndk.gson.Forecast;
import com.clb.school.opencv__ndk.gson.Weather;
import com.clb.school.opencv__ndk.utils.HttpUtil;
import com.clb.school.opencv__ndk.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/24.
 */

public class FragmentWeather extends Fragment {

    private ScrollView weatherLayout;

    public SwipeRefreshLayout swipeRefresh;

    private TextView titleCityName;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_weather,viewGroup,false);
        weatherLayout = (ScrollView)view.findViewById(R.id.weather_layout);
        titleCityName = (TextView)view.findViewById(R.id.title_city);
        titleUpdateTime = (TextView)view.findViewById(R.id.title_update_time);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        degreeText = (TextView)view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView)view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)view.findViewById(R.id.forecast_layout);
        aqiText = (TextView)view.findViewById(R.id.aqi_text);
        pm25Text = (TextView)view.findViewById(R.id.pm25_text);
        comfortText = (TextView)view.findViewById(R.id.comfort_info);
        sportText = (TextView)view.findViewById(R.id.sport_text);
        carWashText = (TextView)view.findViewById(R.id.car_wash_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String weatherString = prefs.getString("weather",null);
        final String weatherId;
        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            weatherLayout.setVisibility(View.INVISIBLE);//先将界面隐藏
            requestWeather();
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                requestWeather();
            }
        });
        return view;
    }


    /**
     * 根据天气id查询天气信息
     */
    public void requestWeather(){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=CN101220104&key=053210198ef745a8b1f404ae34860e27";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"获取天气信息失败",Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(getActivity(),"获取天气信息失败",Toast.LENGTH_LONG).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    /**
     * 获取并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[0];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCityName.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();//更新未来几天的天气预报
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            minText.setText(forecast.temperature.min+ "℃");
            maxText.setText(forecast.temperature.max+ "℃");
            forecastLayout.addView(view);
        }
        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动指数：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        sportText.setText(sport);
        carWashText.setText(carWash);
        weatherLayout.setVisibility(View.VISIBLE);
    }

}
