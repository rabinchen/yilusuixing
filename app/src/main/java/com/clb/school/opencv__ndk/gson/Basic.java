package com.clb.school.opencv__ndk.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/25.
 */
public class Basic {

    //使用GSON提供的注解，方便对象成员变量与json数据中的字段一一对应
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;

    }
}
