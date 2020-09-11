package com.example.misesiva;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.misesiva.utils.Constant;
import com.example.misesiva.utils.Curl;
import com.example.misesiva.utils.GeoPoint;
import com.example.misesiva.utils.GeoTrans;
import com.example.misesiva.utils.TabPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText txtResult;
    Context mContext;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout1);
        tabLayout.addTab(tabLayout.newTab().setText("오늘"));
        tabLayout.addTab(tabLayout.newTab().setText("내일"));
        tabLayout.addTab(tabLayout.newTab().setText("주간"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing ViewPager
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        //Creating adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // 현위치 위도 , 경도
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            try {
                List<Address> mResultList = geocoder.getFromLocation(latitude,longitude,1);
                String cut[] = mResultList.get(0).getAddressLine(0).split(" ");
                for(int i=0; i<cut.length; i++){
                    System.out.println("cut["+i+"] : " + cut[i]);
                }
                Log.e(TAG,"주소 = " + mResultList.get(0).getAddressLine(0));
                Constant.place = cut[2] +" "+ cut[3] +" "+ cut[4];
            } catch (IOException e) {
                e.printStackTrace();
            }
            GeoPoint in_pt = new GeoPoint(longitude, latitude);
            GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);
            Log.e(TAG,"tm : xTM=" + tm_pt.getX() + ", yTM=" + tm_pt.getY());
//            Constant.state = "위치정보 : " + provider + "\n" +
//                    "위도 : " + longitude + "\n" +
//                    "경도 : " + latitude + "\n" +
//                    "고도  : " + altitude + "\n" +
//                    "xTM  : " + tm_pt.getX() + "\n" +
//                    "yTM  : " + tm_pt.getY();
            getState(tm_pt.getX(),tm_pt.getY());

//             GPS 변화가 감지될때 gpsLocationListener 실행
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
        }

    }

    public void getState(Double tmX, Double tmY) {
        String res = Curl.getInSeparateThread("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX="+ tmX +"&tmY="+ tmY +"&pageNo=1&numofRows=10&ServiceKey=ilkRg4UJ9VM8g7L8nu3YD2dXVMnHNTsDOvGjhOxYjvA62DBnotXe91ErmhQDThtqKmDkrOUYCpeWISQs%2BL%2F%2Bmw%3D%3D&_returnType=json","Content-Type:application/json;charset-UTF-8");
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray data = jsonObject.getJSONArray("list");
            Log.e(TAG,data.toString());
            String first = data.getString(0);
            JSONObject fState = new JSONObject(first);
            String state = fState.getString("stationName");
            getData(state);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void getData(String state) {
        String res = Curl.getInSeparateThread("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName="+ state +"&dataTerm=daily&pageNo=1&numOfRows=10&ServiceKey=ilkRg4UJ9VM8g7L8nu3YD2dXVMnHNTsDOvGjhOxYjvA62DBnotXe91ErmhQDThtqKmDkrOUYCpeWISQs%2BL%2F%2Bmw%3D%3D&ver=1.3&_returnType=json","Content-Type:application/json;charset-UTF-8");
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray data = jsonObject.getJSONArray("list");
            Log.e(TAG,data.toString() + state);
            String first = data.getString(0);
            JSONObject fState = new JSONObject(first);
            Integer miseValue = fState.getInt("pm10Value");
            Integer chomiseValue = fState.getInt("pm25Value");
            Constant.mise = miseValue;
            Constant.chomise = chomiseValue;

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            GeoPoint in_pt = new GeoPoint(longitude, latitude);
            GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);
            Log.e(TAG,"ttm : xTM=" + tm_pt.getX() + ", yyTM=" + tm_pt.getY());
//            Constant.state = "위치정보 : " + provider + "\n" +
//                    "위도 : " + longitude + "\n" +
//                    "경도 : " + latitude + "\n" +
//                    "고도  : " + altitude + "\n" +
//                    "xTM  : " + tm_pt.getX() + "\n" +
//                    "yTM  : " + tm_pt.getY();m
            getState(tm_pt.getX(),tm_pt.getY());

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

}
