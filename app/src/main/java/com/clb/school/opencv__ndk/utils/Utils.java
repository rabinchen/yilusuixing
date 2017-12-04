package com.clb.school.opencv__ndk.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/24.
 */

public class Utils {
    public static void showToast(Context context,CharSequence text){
        Toast toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
