package com.clb.school.opencv__ndk;

import android.app.Application;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MyApplication extends Application {

    // Used to load the 'native-lib' library on application startup.
    static {
        //System.loadLibrary("native-lib");
        System.loadLibrary("processFrame");
    }



    @Override
    public void onCreate(){
        super.onCreate();
    }



}
