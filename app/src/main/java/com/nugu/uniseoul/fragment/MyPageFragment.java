package com.nugu.uniseoul.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.data.ReviewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MyPageFragment extends Fragment {

    String user_name;
    String user_email;
    FirebaseAuth mAuth;
    Bitmap bitmap;

    private int count_review = 0;
    private int count_vol = 0;

    LinearLayout pageLayout;
    TextView volCount;
    TextView reviewCount;
    ImageView refresh;

    public MyPageFragment() {
        System.out.println("frag" + user_name + user_email);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);




        mAuth = FirebaseAuth.getInstance( );
        final FirebaseUser user = mAuth.getCurrentUser( );

        user_name = user.getDisplayName( );
        user_email = user.getEmail( );

        CircularImageView user_profile = viewGroup.findViewById(R.id.profile_view);

        TextView name_text = viewGroup.findViewById(R.id.my_page_name);
        TextView email_text = viewGroup.findViewById(R.id.my_page_email);
        LinearLayout pageLayout = viewGroup.findViewById(R.id.page_frament_layout);


        name_text.setText(user_name);
        email_text.setText(user_email);

        TextView volCount = viewGroup.findViewById(R.id.vol_count);
        TextView reviewCount = viewGroup.findViewById(R.id.review_count);

        //ImageView refresh = viewGroup.findViewById(R.id.refresh_btn);

        /*
        refresh.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
         */

        Thread mThread = new Thread( ) {
            @Override
            public void run() {
                try {
                    //현재로그인한 사용자 정보를 통해 PhotoUrl 가져오기
                    URL url = new URL(user.getPhotoUrl( ).toString( ));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection( );
                    conn.setDoInput(true);
                    conn.connect( );
                    InputStream is = conn.getInputStream( );
                    bitmap = BitmapFactory.decodeStream(is);
                }
                catch(NullPointerException eee){
                    eee.printStackTrace();
                }
                catch (MalformedURLException ee) {
                    ee.printStackTrace( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        };
        mThread.start( );
        try {
            mThread.join( );
            //변환한 bitmap적용
            user_profile.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace( );
        }

        System.out.println("test before Thread");
        Thread myThread= new Thread() {
            public void run() {
                System.out.println("test before Thread");
                parseCount(user_email);
            }
        };
        myThread.start();
        try{
            myThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(count_review);
        System.out.println(count_vol);

        String r = Integer.toString(count_review);
        String v = Integer.toString(count_vol);

        reviewCount.setText(r);
        volCount.setText(v);


        return viewGroup;
    }


    private void refresh(){
        FragmentTransaction transaction = getFragmentManager( ).beginTransaction( );
        transaction.detach(this).attach(this).commit( );
    }

    protected void parseCount(String user_email) {

        String receiveMsg;


        //한글 cid encoading
        try {
            user_email = URLEncoder.encode(user_email, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace( );
        }

        String url = "http://15.164.80.191:3000/count_review/";
        url = url.concat(user_email);
        Log.d("url", url);


        String result = ""; //

        InputStream is = null;
        try {
            is = new URL(url).openStream( );
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String str;
            StringBuffer buffer = new StringBuffer( );
            while ((str = rd.readLine( )) != null) {
                buffer.append(str);
            }

            try {
                JSONObject jsonObj;
                jsonObj = new JSONObject(buffer.toString( ));
                Log.d("test", buffer.toString( ));
                JSONArray arrayDocs = jsonObj.getJSONArray("docs");

                List<ReviewData> reviewDatas = new ArrayList<>( );

                count_review = arrayDocs.length();

            } catch (JSONException e) {
                e.printStackTrace( );
            }

        } catch (java.io.FileNotFoundException e1) {

            e1.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }

        url = "http://15.164.80.191:3000/count_vol/";
        url = url.concat(user_email);
        Log.d("url", url);


        result = ""; //

        is = null;
        try {
            is = new URL(url).openStream( );
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String str;
            StringBuffer buffer = new StringBuffer( );
            while ((str = rd.readLine( )) != null) {
                buffer.append(str);
            }

            try {
                JSONObject jsonObj;
                jsonObj = new JSONObject(buffer.toString( ));
                Log.d("test", buffer.toString( ));
                JSONArray arrayDocs = jsonObj.getJSONArray("docs");


                count_vol = arrayDocs.length();

            } catch (JSONException e) {
                e.printStackTrace( );
            }

        } catch (java.io.FileNotFoundException e1) {

            e1.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }


    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            refresh();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
