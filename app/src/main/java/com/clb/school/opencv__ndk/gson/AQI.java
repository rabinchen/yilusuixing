package com.clb.school.opencv__ndk.gson;

/**
 * Created by Administrator on 2017/11/25.
 */


//检测空气质量
public class AQI {

    public AQICity city;

    public class AQICity{

        public String aqi;

        public String pm25;

    }
}