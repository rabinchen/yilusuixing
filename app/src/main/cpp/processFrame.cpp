//
// Created by Administrator on 2017/8/31.
//
#include<jni.h>
#include<iostream>
#include<opencv2/opencv.hpp>
#include<opencv2/core/core.hpp>
#include<opencv2/highgui/highgui.hpp>
#include<opencv2/imgproc/imgproc.hpp>
using namespace cv;
using namespace std;
extern "C"
{

//java层输入每一帧图片
JNIEXPORT void JNICALL Java_com_clb_school_opencv_1_1ndk_view_CameraActivity_processFrames
        (JNIEnv *env, jobject instance, jlong addrInRGBA, jlong addrOut) {
    Mat *pMatInRGBA = (Mat *) addrInRGBA;
    Mat *pMatOut = (Mat *) addrOut;
    Mat BGZimg, imageGray, imageBlur, imageEr, imageCanny;
    (*pMatInRGBA).copyTo(BGZimg);
//不规则处理
    Point points[1][6];
    points[0][0] = Point(BGZimg.cols / 4, BGZimg.rows / 2);
    points[0][1] = Point(BGZimg.cols * 3 / 4, BGZimg.rows / 2);
    points[0][2] = Point(BGZimg.cols, BGZimg.rows);
    points[0][3] = Point(BGZimg.cols, 0);
    points[0][4] = Point(0, 0);
    points[0][5] = Point(0, BGZimg.rows);
    const Point *pt[1] = {points[0]};
    int npt[1] = {6};
    polylines(BGZimg, pt, npt, 1, 1, Scalar(0, 0, 0, 0), 1, 8, 0);
    fillPoly(BGZimg, pt, npt, 1, Scalar(0, 0, 0));

//灰度化图片
    cvtColor(BGZimg, imageGray, CV_RGBA2GRAY);
//高斯模糊
    GaussianBlur(imageGray, imageBlur, Size(3, 3), 0, 0);
//二值化图像
    threshold(imageBlur, imageEr, 200, 255, CV_THRESH_BINARY);
//边缘检测
    Canny(imageEr, imageCanny, 140, 250, 3);

    vector<Vec4i> lines;
//霍夫直线检测
    HoughLinesP(imageCanny, lines, 1, CV_PI / 180, 175, 150, 15);
    for (size_t i = 0; i < lines.size(); i++) {
        line(*pMatInRGBA, Point(lines[i][0], lines[i][1]), Point(lines[i][2], lines[i][3]),
             Scalar(0, 255, 0), 5, 8);
    }
    *pMatOut = *pMatInRGBA;
}
}

