package com.clb.school.opencv__ndk.utils;

import android.text.TextUtils;

import com.clb.school.opencv__ndk.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/25.
 */

public class Utility {

//    /**
//     * 解析和处理服务器返回的省级数据
//     */
//    public static boolean handleProvinceResponse(String response){
//        if(!TextUtils.isEmpty(response)){
//            try{
//                JSONArray allProvince = new JSONArray(response);//将服务器返回的数据传入到JSONArray对象
//                for(int i = 0;i < allProvince.length();i++){
//                    JSONObject provinceObject = allProvince.getJSONObject(i);//JSONArray对象中存储的是JSONObject对象，然后遍历取出
//                    Province province = new Province();
//                    province.setProvinceName(provinceObject.getString("name"));
//                    province.setProvinceCode(provinceObject.getInt("id"));
//                    province.save();
//                }
//                return true;
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 解析和处理服务器返回的市级数据
//     */
//    public static boolean handleCityResponse(String response,int provinceId){
//        if(!TextUtils.isEmpty(response)){
//            try{
//                JSONArray allCities = new JSONArray(response);
//                for(int i = 0;i < allCities.length();i++){
//                    JSONObject cityObject = allCities.getJSONObject(i);
//                    City city = new City();
//                    city.setCityName(cityObject.getString("name"));
//                    city.setCityCode(cityObject.getInt("id"));
//                    city.setProvinceId(provinceId);
//                    city.save();
//                }
//                return true;
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    /*shu'ju*
//     * 解析和处理服务器返回的县级数据
//     */
//    public static boolean handleCountyResponse(String response,int cityId){
//        if(!TextUtils.isEmpty(response)){
//            try{
//                JSONArray allCounties = new JSONArray(response);
//                for(int i = 0;i < allCounties.length();i++){
//                    JSONObject countyObject = allCounties.getJSONObject(i);
//                    County county = new County();
//                    county.setCountyName(countyObject.getString("name"));
//                    county.setWeatherId(countyObject.getString("weather_id"));
//                    county.setCityId(cityId);
//                    county.save();
//                }
//                return true;
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    /**
     *将返回的json数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);//将json数据转化为Weather对象
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
