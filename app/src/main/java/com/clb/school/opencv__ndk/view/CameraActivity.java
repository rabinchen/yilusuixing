package com.clb.school.opencv__ndk.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.clb.school.opencv__ndk.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

/**
 * Created by Administrator on 2017/8/31.
 */

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private int requestCode = 1;
    private CameraBridgeViewBase mOpenCvCameraView;
    private String TAG = CameraActivity.class.getSimpleName();

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.CameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableView();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    private int count = 0;
    private int geFrame = 5;
    private long startTime;
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat output = new Mat();
        Mat input = inputFrame.rgba();
//        count++;
//        if(count % geFrame == 0){
//            processFrames(input.getNativeObjAddr(),output.getNativeObjAddr());
//            return output;
//        }
        startTime = System.nanoTime();
        processFrames(input.getNativeObjAddr(),output.getNativeObjAddr());
        long consumeTime = System.nanoTime() - startTime;
        Log.i(TAG,String.valueOf(consumeTime/1000)+"ms");
        return input;
    }

    public native void processFrames(long matAddrInRGBA,long matAddrOutInRGBA);
}
