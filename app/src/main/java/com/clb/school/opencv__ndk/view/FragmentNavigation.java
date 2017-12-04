package com.clb.school.opencv__ndk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCarInfo;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapRestrictionInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.PoiInputItemWidget;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.clb.school.opencv__ndk.R;
import com.clb.school.opencv__ndk.searchlocation.SearchPoiActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class FragmentNavigation extends Fragment implements AMapNaviListener,View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private boolean congestion, cost, hightspeed, avoidhightspeed;

    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;

    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();//开始坐标点
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();//结束坐标点
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    /**
     * 当前用户选中的路线，在下个页面进行导航
     */
    private int routeIndex;
    /**
     * 路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线
     **/
    private int zindex = 1;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;
    private boolean chooseRouteSuccess = false;
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);

    private Button startNavigation,startBt,endBt,calculateBt;

    private String startLocation;
    private String endLocation;
    private CheckBox congestionBt,costBt,avoidhightspeedBt,hightspeedBt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_navigation,viewGroup,false);
        startNavigation = (Button)view.findViewById(R.id.startNavigation);
        startBt = (Button)view.findViewById(R.id.input_start);
        endBt = (Button)view.findViewById(R.id.input_end);
        calculateBt = (Button)view.findViewById(R.id.calculateRute);
        congestionBt = (CheckBox)view.findViewById(R.id.congestion);
        costBt = (CheckBox)view.findViewById(R.id.cost);
        avoidhightspeedBt = (CheckBox)view.findViewById(R.id.avoidhightspeed);
        hightspeedBt = (CheckBox)view.findViewById(R.id.hightspeed);
        congestionBt.setOnCheckedChangeListener(this);
        costBt.setOnCheckedChangeListener(this);
        avoidhightspeedBt.setOnCheckedChangeListener(this);
        hightspeedBt.setOnCheckedChangeListener(this);
        startNavigation.setOnClickListener(this);
        startBt.setOnClickListener(this);
        endBt.setOnClickListener(this);
        calculateBt.setOnClickListener(this);
        mAMapNavi = AMapNavi.getInstance(getActivity().getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

        return view;
    }

    public void changeRoute() {
        if (!calculateSuccess) {
            Toast.makeText(getActivity(), "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            chooseRouteSuccess = true;
            //必须告诉AMapNavi 你最后选择的哪条路
            mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
            Toast.makeText(getActivity(), "导航距离:" + (mAMapNavi.getNaviPath()).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPath()).getAllTime() + "s", Toast.LENGTH_SHORT).show();
            return;
        }

        if (routeIndex >= routeOverlays.size())
            routeIndex = 0;
        int routeID = routeOverlays.keyAt(routeIndex);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.4f);
        }
        routeOverlays.get(routeID).setTransparency(1);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);

        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
        Toast.makeText(getActivity(), "路线标签:" + mAMapNavi.getNaviPath().getLabels(), Toast.LENGTH_SHORT).show();
        routeIndex++;
        chooseRouteSuccess = true;

        /**选完路径后判断路线是否是限行路线**/
        AMapRestrictionInfo info = mAMapNavi.getNaviPath().getRestrictionInfo();
        if (!TextUtils.isEmpty(info.getRestrictionTitle())) {
            Toast.makeText(getActivity(), info.getRestrictionTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.calculateRute:
                clearRoute();
                if (avoidhightspeed && hightspeed) {
                    Toast.makeText(getActivity(), "不走高速与高速优先不能同时为true.", Toast.LENGTH_LONG).show();
                }
                if (cost && hightspeed) {
                    Toast.makeText(getActivity(), "高速优先与避免收费不能同时为true.", Toast.LENGTH_LONG).show();
                }
            /*
             * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
			 * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
			 */
                int strategyFlag = 0;
                try {
                    strategyFlag = mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strategyFlag >= 0) {
//                    String carNumber = editText.getText().toString();
//                    AMapCarInfo carInfo = new AMapCarInfo();
//                    //设置车牌
//                    carInfo.setCarNumber(carNumber);
//                    //设置车牌是否参与限行算路
//                    carInfo.setRestriction(true);
//                    mAMapNavi.setCarInfo(carInfo);
                    mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                    Toast.makeText(getActivity(), "策略:" + strategyFlag, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.startNavigation:
                Intent intent = new Intent(getActivity(),NavigationActivity.class);
                intent.putExtra("gps", true);
                startActivity(intent);
                break;
            case R.id.input_start:
                Intent sintent = new Intent(getActivity(), SearchPoiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pointType", PoiInputItemWidget.TYPE_START);
                sintent.putExtras(bundle);
                startActivityForResult(sintent, 100);
                break;
            case R.id.input_end:
                Intent eintent = new Intent(getActivity(), SearchPoiActivity.class);
                Bundle ebundle = new Bundle();
                ebundle.putInt("pointType", PoiInputItemWidget.TYPE_DEST);
                eintent.putExtras(ebundle);
                startActivityForResult(eintent, 200);
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch(id){
            case R.id.congestion:
                congestion = isChecked;
                break;
            case R.id.cost:
                cost = isChecked;
                break;
            case R.id.avoidhightspeed:
                avoidhightspeed = isChecked;
                break;
            case R.id.hightspeed:
                hightspeed = isChecked;
                break;
        }
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getParcelableExtra("poi") != null) {
            clearRoute();
            Poi poi = data.getParcelableExtra("poi");
            if (requestCode == 100) {//起点选择完成
                //Toast.makeText(this, "100", Toast.LENGTH_SHORT).show();
                startLatlng = new NaviLatLng(poi.getCoordinate().latitude, poi.getCoordinate().longitude);
                //mStartMarker.setPosition(new LatLng(poi.getCoordinate().latitude, poi.getCoordinate().longitude));
                startList.clear();
                startList.add(startLatlng);
                startBt.setText(poi.getName());
            }
            if (requestCode == 200) {//终点选择完成
                //Toast.makeText(this, "200", Toast.LENGTH_SHORT).show();
                endLatlng = new NaviLatLng(poi.getCoordinate().latitude, poi.getCoordinate().longitude);
                //mEndMarker.setPosition(new LatLng(poi.getCoordinate().latitude, poi.getCoordinate().longitude));
                endList.clear();
                endList.add(endLatlng);
                endBt.setText(poi.getName());
            }
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {


    }

    @Override
    public void hideCross() {


    }

    @Override
    public void hideLaneInfo() {


    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int arg0) {


    }

    @Override
    public void onArriveDestination() {


    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onArrivedWayPoint(int arg0) {


    }

    @Override
    public void onEndEmulatorNavi() {


    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {


    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {


    }

    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {


    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {


    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {


    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {


    }

    @Override
    public void onReCalculateRouteForYaw() {


    }

    @Override
    public void onStartNavi(int arg0) {


    }

    @Override
    public void onTrafficStatusUpdate() {


    }

    @Override
    public void showCross(AMapNaviCross arg0) {


    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {


    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {


    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat arg0) {


    }


}
