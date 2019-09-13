package com.nugu.uniseoul;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.adapter.RivewRecyclerViewAdapter;
import com.nugu.uniseoul.data.ReviewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ReadReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ReviewData> reviews;
    String[] reviewData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_review);

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.review_recyclerview);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        final String cid = bundle.getString("cid");


        new Thread() {
            public void run() {
          parse("http://xxx.xxx.xxx.xxx:3000/read_review/",cid);
            }
        }.start();


    }

    protected void parse(String addr, String cid){

        String receiveMsg;

        cid = cid.replace(" ","_");

        //한글 cid encoading
        try{
            cid = URLEncoder.encode(cid,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String url = addr.concat(cid);
        Log.d("url",url);


        String result = ""; //




        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = rd.readLine()) != null) {
                buffer.append(str);
            }

            try {

                JSONObject jsonObj;
                jsonObj = new JSONObject(buffer.toString());
                Log.d("buffer",buffer.toString());
                JSONArray arrayDocs = jsonObj.getJSONArray("docs");

                List<ReviewData> reviews = new ArrayList<>();

                    for (int i = 0, j = arrayDocs.length(); i < j; i++) {
                        JSONObject obj = arrayDocs.getJSONObject(i);

                        Log.d("reviews", obj.toString());
                        //분류
                        ReviewData reviewData = new ReviewData();

                        reviewData.setTitle(obj.getString("title"));
                        reviewData.setContent(obj.getString("content"));
                        reviews.add(reviewData);
                        }

            /*
                else if(buffer.toString().equals("{\"docs\":\"review not found\"}")){
                    ReviewData reviewData = new ReviewData();

                    reviewData.setTitle("등록 된 리뷰가 없습니다.");
                    reviewData.setContent("리뷰를 작성해주세요");
                    reviews.add(reviewData);
                }
                 */

                mAdapter = new RivewRecyclerViewAdapter(reviews, ReadReviewActivity.this);
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (java.io.FileNotFoundException e1) {

            List<ReviewData> reviews = new ArrayList<>();
            ReviewData reviewData = new ReviewData();

            reviewData.setTitle("등록 된 리뷰가 없습니다.");
            reviewData.setContent("\n 첫 리뷰를 작성해주세요 \n" );
            reviews.add(reviewData);

            mAdapter = new RivewRecyclerViewAdapter(reviews, ReadReviewActivity.this);
            recyclerView.setAdapter(mAdapter);


            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
