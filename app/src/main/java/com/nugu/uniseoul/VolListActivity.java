package com.nugu.uniseoul;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nugu.uniseoul.data.VolData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class VolListActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewContent;
    private TextView during_textview;
    private TextView date_textview;
    private TextView place_textview;

    //private Button uni_btn;
    private ImageView vol_btn;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vollist);


        Intent intent = getIntent();

        final VolData volData = (VolData)intent.getSerializableExtra("volData");
        final String user_id = (String)intent.getSerializableExtra("user_id");
        final String user_email = (String)intent.getSerializableExtra("user_email");

        String v_num = volData.getV_num();

        String v_title = volData.getTitle();
        String v_content = volData.getContent();
        String s_date = volData.getS_date();
        String e_date = volData.getE_date();

        String c_helper = volData.getCurrent_helper();
        String m_helper = volData.getMax_helper();

        String c_uni = volData.getCurrent_uni();
        String m_uni = volData.getMax_uni();

        System.out.println("user_id : " + user_id + "\tuser_email : " + user_email);
        System.out.println("v_num : " + v_num + "\tv_title : "  + v_title + "\tv_content : " + v_content );
        System.out.println( " uni : " + c_uni + "/" + m_uni + "\thelper : " + c_helper+ "/" + m_helper );
        System.out.println( " date : " + s_date + "-"+ e_date );

        textViewTitle = (TextView)findViewById(R.id.vol_list_title_textView);
        textViewTitle.setText(v_title);
        textViewContent =  (TextView)findViewById(R.id.vol_list_content_textView);
        textViewContent.setText(v_content);


        during_textview = (TextView)findViewById(R.id.vol_during);
        during_textview.setText("신청 기간 : " + volData.getS_date() + " ~ " + volData.getE_date());

        date_textview = (TextView)findViewById(R.id.vol_date);
        date_textview.setText("봉사 일시 : " + volData.getR_date() + " -> " + volData.getR_time());

        place_textview = (TextView)findViewById(R.id.vol_place);
        place_textview.setText("봉사 장소 : " + volData.getPlace());


        //uni_btn =  (Button)findViewById(R.id.vol_list_uni_btn);
        //uni_btn.setText("신청하기");


        vol_btn =  (ImageView) findViewById(R.id.vol_list_vol_btn);

        /*
        uni_btn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {


                new Thread() {
                    public void run() {
                        Log.d("put uni ",v_num + user_id + user_email);
                        put_uni(v_num,user_id,user_email);
                    }
                }.start();
                Toast.makeText(getApplicationContext(), "신청완료!", Toast.LENGTH_LONG).show();

            }
        });
        */
        vol_btn.setOnClickListener(new View.OnClickListener( ) {
            @Override

            public void onClick(View v) {
                Thread t1 = new Thread() {
                    public void run() {
                        Log.d("put uni ",v_num + user_id + user_email);
                        put_helper(v_num,user_id,user_email);


                    }
                };
                t1.start();
                try{
                    t1.join();
                    if(i ==1)
                        Toast.makeText(getApplicationContext(), "신청완료!", Toast.LENGTH_LONG).show();
                    if(i == 2)
                        Toast.makeText(getApplicationContext(), "이미 신청 되어있습니다.", Toast.LENGTH_LONG).show();
                    if(i ==0)
                        Toast.makeText(getApplicationContext(), "신청 오류.", Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }


            }
        });



    }


    private void put_helper(String v_num, String v_user_id, String v_email) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://15.164.80.191:3000:3000/put_helper_list");
        ArrayList<NameValuePair> nameValues = new ArrayList<NameValuePair>();
        try {
            //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
            nameValues.add(new BasicNameValuePair(
                    "v_num", URLDecoder.decode(v_num, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "v_user_id", URLDecoder.decode(v_user_id, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "v_email", URLDecoder.decode(v_email, "UTF-8")));



            //HttpPost에 넘길 값을들 Set해주기
            post.setEntity(
                    new UrlEncodedFormEntity(
                            nameValues, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("Insert Log", ex.toString());
        }

        try {
            //설정한 URL을 실행시키기
            HttpResponse response = client.execute(post);
            //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
            if(response.getStatusLine().getStatusCode()==200){
                i =1;
            }
            if(response.getStatusLine().getStatusCode()==400){
                i = 2;
            }


            Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void put_uni(String v_num, String v_user_id, String v_email) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://15.164.80.191:3000:3000/put_uni_list");
        ArrayList<NameValuePair> nameValues = new ArrayList<NameValuePair>();
        try {
            //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
            nameValues.add(new BasicNameValuePair(
                    "v_num", URLDecoder.decode(v_num, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "v_user_id", URLDecoder.decode(v_user_id, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "v_email", URLDecoder.decode(v_email, "UTF-8")));



            //HttpPost에 넘길 값을들 Set해주기
            post.setEntity(
                    new UrlEncodedFormEntity(
                            nameValues, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Log.e("Insert Log", ex.toString());
        }

        try {
            //설정한 URL을 실행시키기
            HttpResponse response = client.execute(post);
            //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
            Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
