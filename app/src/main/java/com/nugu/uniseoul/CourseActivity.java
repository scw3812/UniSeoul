package com.nugu.uniseoul;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nugu.uniseoul.data.CourseData;
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

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private ImageView courseImage;
    private TextView textViewContentHompage;
    private TextView textViewContentTel;
    private Button mapButton;
    private ImageView[] courseBarrierFree;
    private TextView reviewTitle;
    private TextView reviewContent;
    private LinearLayout reviewLayout;
    private ReviewData reviewData;
    private GridLayout gridLayout;
    private ImageView[] courseTripBarrierFree;
    private String[] tripBarrierFreeArray;
    private ImageView courseHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        final CourseData courseData = (CourseData)intent.getSerializableExtra("course");
        String contentHompage = null;
        if(!courseData.getCourseHomepage().equals("null")){
            contentHompage = "홈페이지 "+courseData.getCourseHomepage();
        }else{
            contentHompage = "홈페이지가 없습니다.";
        }
        String contentTel = null;
        if(!courseData.getCourseTel().equals("null")){
            contentTel = "Tel."+courseData.getCourseTel();
        }else{
            contentTel = "전화번호가 없습니다.";
        }
        tripBarrierFreeArray = null;
        if(courseData.getCourseTripBarrierFree() != null){
            String tripBarrierFree = null;
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                tripBarrierFree = Html.fromHtml(courseData.getCourseTripBarrierFree()).toString();
            }else {
                tripBarrierFree = Html.fromHtml(courseData.getCourseTripBarrierFree(), Html.FROM_HTML_MODE_LEGACY).toString();
            }
            tripBarrierFreeArray = tripBarrierFree.split("!");
        }
        String title = courseData.getCourseTitle();
        String[] barrierFree = courseData.getCourseBarrierFree();

        textViewTitle = (TextView)findViewById(R.id.course_title);
        courseImage = (ImageView)findViewById(R.id.course_image);
        textViewContentHompage = (TextView)findViewById(R.id.course_content_hompage);
        textViewContentTel = (TextView)findViewById(R.id.course_content_tel);
        mapButton = (Button)findViewById(R.id.course_map_button);
        courseBarrierFree = new ImageView[]{(ImageView)findViewById(R.id.course_barrierfree1),(ImageView)findViewById(R.id.course_barrierfree2),
                (ImageView)findViewById(R.id.course_barrierfree3),(ImageView)findViewById(R.id.course_barrierfree4),
                (ImageView)findViewById(R.id.course_barrierfree5),(ImageView)findViewById(R.id.course_barrierfree6),
                (ImageView)findViewById(R.id.course_barrierfree7),(ImageView)findViewById(R.id.course_barrierfree8),
                (ImageView)findViewById(R.id.course_barrierfree9),(ImageView)findViewById(R.id.course_barrierfree10),
                (ImageView)findViewById(R.id.course_barrierfree11),(ImageView)findViewById(R.id.course_barrierfree12)};
        gridLayout = (GridLayout)findViewById(R.id.course_barrierfree_grid);
        courseTripBarrierFree = new ImageView[]{(ImageView)findViewById(R.id.course_notice_parking),(ImageView)findViewById(R.id.course_notice_toilet),
                (ImageView)findViewById(R.id.course_notice_wheelchair),(ImageView)findViewById(R.id.course_notice_braille),
                (ImageView)findViewById(R.id.course_notice_lent),(ImageView)findViewById(R.id.course_notice_slope)};
        courseHeart = (ImageView)findViewById(R.id.course_heart);

        textViewTitle.setText(title);
        if(tripBarrierFreeArray == null){
            courseHeart.setVisibility(View.GONE);
        }
        Glide.with(this).load(courseData.getCourseImage()).placeholder(R.drawable.uniseoul_placeholder).into(courseImage);
        textViewContentHompage.setText(contentHompage);
        textViewContentTel.setText(contentTel);

        //무장애 여행 아이콘 구현 및 클릭 이벤트 설정
        for(int i = 0; i<barrierFree.length; i++){
            if(barrierFree[i].equals("N")){
                courseBarrierFree[i].setVisibility(View.GONE);
                continue;
            }
            courseBarrierFree[i].setOnClickListener(this);
        }

        if(tripBarrierFreeArray == null){
            gridLayout.setVisibility(View.GONE);
        }else{
            for(int i = 0; i<courseTripBarrierFree.length; i++){
                final int num = i;
                courseTripBarrierFree[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CourseActivity.this,tripBarrierFreeArray[num*2+1],Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        String cid = title;
        reviewData = new ReviewData();

        reviewTitle = findViewById(R.id.course_review_title);
        reviewContent = findViewById(R.id.course_review_content);


        System.out.println("start");
        Thread mThread= new Thread() {
            public void run() {
                System.out.println("thread start");
                parseReview("http://15.164.80.191:3000/read_review/",cid);
            }
        };
        try{
            mThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        mThread.start();

        reviewLayout = findViewById(R.id.course_review_layout);

        reviewLayout.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this,ReadReviewActivity.class);
                intent.putExtra("cid",title);
                intent.putExtra("course",courseData);
                startActivity(intent);
                finish();
            }
        });

        //위치 버튼 클릭 이벤트
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this,MapActivity.class);
                intent.putExtra("course",courseData);
                startActivity(intent);
            }
        });
    }

    //무장애 여행 아이콘 클릭 이벤트
    @Override
    public void onClick(View v) {
        ImageView imageView = (ImageView) v;
        String[] barrierfreeString = new String[]{"주출입구 접근로 있음", "장애인 전용 주차구역 있음", "주출입구 높이차이 제거",
                "장애인용 승강기 있음", "장애인 화장실 있음", "장애인용 객실 이용가능", "장애인용 관람석 이용가능",
                "장애인 매표소 있음", "시각장애인 편의서비스 있음", "청각장애인 편의서비스 있음", "안내 서비스 있음", "휠체어 대여 가능"};
        for(int i = 0; i < courseBarrierFree.length; i++){
            if(courseBarrierFree[i] == imageView){
                Toast.makeText(this,barrierfreeString[i],Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void parseReview(String addr, String cid){

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

                JSONObject obj = arrayDocs.getJSONObject(0);

                Log.d("review", obj.toString());
                //분류

                reviewData.setTitle(obj.getString("title"));
                reviewData.setContent(obj.getString("content"));
                reviewData.setEmail(obj.getString("user_email"));

                reviewTitle.setText(reviewData.getTitle());
                reviewContent.setText(reviewData.getContent());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (java.io.FileNotFoundException e1) {


            reviewData.setTitle("등록 된 리뷰가 없습니다.");
            reviewData.setContent("첫 리뷰를 작성해주세요" );

            reviewTitle.setText(reviewData.getTitle());
            reviewContent.setText(reviewData.getContent());

            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
