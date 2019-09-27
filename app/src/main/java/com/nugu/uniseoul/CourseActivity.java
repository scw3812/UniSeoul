package com.nugu.uniseoul;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nugu.uniseoul.data.CourseData;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private ImageView courseImage;
    private TextView textViewContentHompage;
    private TextView textViewContentTel;
    private ImageButton mapButton;
    private ImageView[] courseBarrierFree;
    private Button readReviewBtn;
    private Button writeReviewBtn;
    private TextView textCourseTripBarrierFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        final CourseData courseData = (CourseData)intent.getSerializableExtra("course");
        String contentHompage = "홈페이지가 없습니다.";
        if(courseData.getCourseHomepage() != null){
            contentHompage = "홈페이지 "+courseData.getCourseHomepage();
        }
        String contentTel = "전화번호가 없습니다.";
        if(courseData.getCourseTel() != null){
            contentTel = "Tel."+courseData.getCourseTel();
        }
        String tripBarrierFree = null;
        if(courseData.getCourseTripBarrierFree() != null){
            tripBarrierFree = courseData.getCourseTripBarrierFree();
        }
        String title = courseData.getCourseTitle();
        String[] barrierFree = courseData.getCourseBarrierFree();

        textViewTitle = (TextView)findViewById(R.id.course_title);
        courseImage = (ImageView)findViewById(R.id.course_image);
        textViewContentHompage = (TextView)findViewById(R.id.course_content_hompage);
        textViewContentTel = (TextView)findViewById(R.id.course_content_tel);
        mapButton = (ImageButton)findViewById(R.id.course_map_button);
        courseBarrierFree = new ImageView[]{(ImageView)findViewById(R.id.course_barrierfree1),(ImageView)findViewById(R.id.course_barrierfree2),
                (ImageView)findViewById(R.id.course_barrierfree3),(ImageView)findViewById(R.id.course_barrierfree4),
                (ImageView)findViewById(R.id.course_barrierfree5),(ImageView)findViewById(R.id.course_barrierfree6),
                (ImageView)findViewById(R.id.course_barrierfree7),(ImageView)findViewById(R.id.course_barrierfree8),
                (ImageView)findViewById(R.id.course_barrierfree9),(ImageView)findViewById(R.id.course_barrierfree10),
                (ImageView)findViewById(R.id.course_barrierfree11),(ImageView)findViewById(R.id.course_barrierfree12)};
        textCourseTripBarrierFree = (TextView)findViewById(R.id.course_trip_barrierfree);

        textViewTitle.setText(title);
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

        if(tripBarrierFree != null){
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                textCourseTripBarrierFree.setText(Html.fromHtml(tripBarrierFree).toString());
            }else {
                textCourseTripBarrierFree.setText(Html.fromHtml(tripBarrierFree, Html.FROM_HTML_MODE_LEGACY).toString());
            }
        }

        writeReviewBtn = findViewById(R.id.writeReviewBtn);
        readReviewBtn = findViewById(R.id.readReviewBtn);

        writeReviewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this,WriteReviewActivity.class);
                Log.d("cid",title);
                intent.putExtra("cid",title);
                startActivity(intent);
            }
        });

        readReviewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this,ReadReviewActivity.class);
                intent.putExtra("cid",title);
                startActivity(intent);
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
}
