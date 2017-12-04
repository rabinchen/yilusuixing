package com.clb.school.opencv__ndk.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.clb.school.opencv__ndk.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {



    final String TAG = "MainActivity";
    private SpaceTabLayout spaceTabLayout;
    private FragmentNavigation fragmentNavigation;
    private FragmentWeather fragmentWeather;
    private FragmentLDW fragmentLDW;
    private List<Fragment> fragmentList;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentNavigation = new FragmentNavigation();
        fragmentLDW = new FragmentLDW();
        fragmentWeather = new FragmentWeather();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragmentNavigation);
        fragmentList.add(fragmentLDW);
        fragmentList.add(fragmentWeather);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        spaceTabLayout = (SpaceTabLayout)findViewById(R.id.spaceTablayout);
        spaceTabLayout.initialize(viewPager,getSupportFragmentManager(),fragmentList,null);
        runPermission(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
   // public native int[] grayProc(int[] pixels,int w,int h);

    @Override
    public void onResume(){
        super.onResume();

        if(!OpenCVLoader.initDebug()){
            //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,mloaderCallback);
        }else{
            mloaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public BaseLoaderCallback mloaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG,"OpenCV Manager加载成功");
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Camera:
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WAKE_LOCK,Manifest.permission.WRITE_SETTINGS,Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //管理运行时权限
    private void runPermission(Activity context) {

        ActivityCompat.requestPermissions(MainActivity.this,
                permissions, 1);
//
//        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//
//        }

//        boolean permission;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            permission = Settings.System.canWrite(context);
//        }else{
//            permission = ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
//        }
//        if(permission){
//
//        }else{
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:"+context.getPackageName()));
//                context.startActivityForResult(intent,1);
//            }else{
//                ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.WRITE_SETTINGS},2);
//            }
//        }
    }

//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode == 1){
//            Log.i(TAG,"获取写入设置权限成功");
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResult){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);
        if(requestCode == 2 && grantResult[1] == PackageManager.PERMISSION_GRANTED){

        }
    }

}
