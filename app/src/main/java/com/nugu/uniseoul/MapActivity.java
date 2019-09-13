package com.nugu.uniseoul;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.nugu.uniseoul.Task.GeocoderTask;
import com.nugu.uniseoul.data.CourseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private String[] address;
    double lat = 0;
    double lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        CourseData courseData = (CourseData)intent.getSerializableExtra("course");
        address = new String[]{courseData.getCourseAddress()};

        //지도 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    //지도 생성 메서드
    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        String json = "";

        try {
            json = new GeocoderTask().execute(address).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray("addresses");
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                lat = Double.parseDouble(obj.getString("y"));
                lon = Double.parseDouble(obj.getString("x"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng latLng = new LatLng(lat, lon);
        CameraPosition cameraPosition = new CameraPosition(latLng, 13);
        naverMap.setCameraPosition(cameraPosition);

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(MapActivity.this) {
            @NonNull
            @Override
            public View getContentView(@NonNull InfoWindow infoWindow) {
                LinearLayout linearLayout = new LinearLayout(MapActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView textView = new TextView(MapActivity.this);
                if(infoWindow.getMarker().getTag() == address[0]){
                    textView.setText((CharSequence)infoWindow.getMarker().getTag());
                }else {
                    textView.setText(infoWindow.getMarker().getTag().toString().split(",")[0]+" 정류장");
                }
                linearLayout.addView(textView);
                if(infoWindow.getMarker().getTag() != address[0]){
                    Button button = new Button(MapActivity.this);
                    button.setText("경유저상버스");
                    linearLayout.addView(button);
                }
                return linearLayout;
            }
        });
        infoWindow.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                if(infoWindow.getMarker().getTag() != address[0]){
                    Intent intent = new Intent(MapActivity.this, TransportActivity.class);
                    intent.putExtra("arsId",infoWindow.getMarker().getTag().toString().split(",")[1]);
                    startActivity(intent);
                }
                return false;
            }
        });

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        // 마커를 클릭하면:
        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker)overlay;

            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }

            return true;
        };

        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lon));
        marker.setTag(address[0]);
        marker.setOnClickListener(listener);
        marker.setMap(naverMap);

        new Thread(new Runnable() {

            @Override
            public void run() {

                // TODO Auto-generated method stub
                final String[] stations = getBusStationXmlData(lat,lon).split("\n");
                Log.d("station",getBusStationXmlData(lat,lon));
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        for(int i = 3; i<stations.length; i+=4){
                            double latStation = Double.parseDouble(stations[i-1]);
                            double lonStation = Double.parseDouble(stations[i-2]);
                            Marker markerStation = new Marker();
                            markerStation.setPosition(new LatLng(latStation,lonStation));
                            markerStation.setIcon(MarkerIcons.BLACK);
                            markerStation.setTag(stations[i]+","+stations[i-3]);
                            markerStation.setOnClickListener(listener);
                            markerStation.setMap(naverMap);
                        }
                    }
                });
            }
        }).start();
    }

    String getBusStationXmlData(double lat, double lon) {
        String json = "";

        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey=공공데이터API키&tmX=" + lon + "&tmY=" + lat + "&radius=600";
        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("itemList")) ;// 첫번째 검색결과
                        else if (tag.equals("gpsX")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//gpsX 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("gpsY")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//gpsY 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("arsId")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//arsId 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("stationNm")) {
                            xpp.next();
                            buffer.append(xpp.getText());//stationNm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("itemList")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }
}

