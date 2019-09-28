package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SubwayActivity extends AppCompatActivity {

    private String frCode;
    private String subwayData[];
    private TextView stationNmtextView;
    private ImageView elevator, escalator, wheelchair;
    private TextView toiletLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subway);

        stationNmtextView = (TextView)findViewById(R.id.subway_station_name);
        elevator = (ImageView)findViewById(R.id.subway_elevator);
        escalator = (ImageView)findViewById(R.id.subway_escalator);
        wheelchair = (ImageView)findViewById(R.id.subway_wheelchair);
        toiletLocation = (TextView) findViewById(R.id.subway_toilet_location);

        subwayData = getIntent().getStringExtra("subwayData").split(",");

        stationNmtextView.setText(subwayData[0]+"역");

        String json = null;
        try {
            InputStream is = getAssets().open("subway.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("DATA");

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if(subwayData[0].equals(obj.getString("station_nm"))){
                    if(subwayData[1].equals(obj.getString("line_num"))){
                        frCode = obj.getString("fr_code");
                    }else{
                        if(frCode == null){
                            frCode = obj.getString("fr_code");
                        }
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] subwayXmlData = getSubwayXmlData(frCode).split("\n");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(subwayXmlData[0].equals("Y")){
                            elevator.setImageResource(R.drawable.subway_elevator);
                        }
                        if(subwayXmlData[1].equals("Y")){
                            escalator.setImageResource(R.drawable.subway_escalator);
                        }
                        if(subwayXmlData[2].equals("Y")){
                            wheelchair.setImageResource(R.drawable.subway_wheelchair);
                        }
                        toiletLocation.setText("   "+subwayXmlData[3]+"   ");
                    }
                });
            }
        }).start();
    }

    String getSubwayXmlData(String frCode){
        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://openapi.seoul.go.kr:8088/"+getString(R.string.subway_api_key2)+"/xml/SearchSTNInfoByFRCodeService/1/1/"+frCode;
        Log.d("url", queryUrl);
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

                        if (tag.equals("row")) ;// 첫번째 검색결과
                        else if (tag.equals("ELEVATOR")) {
                            xpp.next();
                            buffer.append(xpp.getText()+"\n");
                        }
                        else if (tag.equals("ESCALATOR")) {
                            xpp.next();
                            buffer.append(xpp.getText()+"\n");
                        }
                        else if (tag.equals("WHEELCHAIR")) {
                            xpp.next();
                            buffer.append(xpp.getText()+"\n");
                        }
                        else if (tag.equals("TOILET")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        Log.d("buffer",buffer.toString());
        return buffer.toString();
    }
}
