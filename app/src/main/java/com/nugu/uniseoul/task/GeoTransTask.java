package com.nugu.uniseoul.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GeoTransTask extends AsyncTask<String,Void,String> {

    String appKey;// 애플리케이션 키값";
    private String str, receiveMsg;

    public GeoTransTask(String appKey){
        this.appKey = appKey;
    }
    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("https://dapi.kakao.com/v2/local/geo/transcoord.json?x="+params[0]+"&y="+params[1]+"&input_coord=WTM&output_coord=WGS84");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK "+appKey);

            if (con.getResponseCode() == 200) {
                InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", con.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}
