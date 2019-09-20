package com.nugu.uniseoul;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nugu.uniseoul.data.CourseData;
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

    private TextView textViewS_date;
    private TextView textViewE_date;

    private Button uni_btn;
    private Button vol_btn;


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
        textViewS_date =  (TextView)findViewById(R.id.vol_list_s_date_textView);
        textViewS_date.setText(s_date);
        textViewE_date =  (TextView)findViewById(R.id.vol_list_e_date_textView);
        textViewE_date.setText(e_date);


        uni_btn =  (Button)findViewById(R.id.vol_list_uni_btn);
        uni_btn.setText("신청 인원 " + c_uni + "/" + m_uni );


        vol_btn =  (Button)findViewById(R.id.vol_list_vol_btn);
        vol_btn.setText("신청 인원 " + c_helper + "/" + m_helper );

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

        vol_btn.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                new Thread() {
                    public void run() {
                        Log.d("put uni ",v_num + user_id + user_email);
                        put_helper(v_num,user_id,user_email);
                    }
                }.start();
                Toast.makeText(getApplicationContext(), "신청완료!", Toast.LENGTH_LONG).show();

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
