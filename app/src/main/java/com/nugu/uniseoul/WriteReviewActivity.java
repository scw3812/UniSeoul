package com.nugu.uniseoul;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nugu.uniseoul.data.CourseData;

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

public class WriteReviewActivity extends AppCompatActivity {

    ImageView sendPostData;
    EditText titleEditText;
    EditText contentEditText;
    FirebaseAuth mAuth;
    String saveCid;
    CourseData saveCourseData;

    String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        user_email = user.getEmail();

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String cid = bundle.getString("cid");
        saveCid = cid;
        Log.d("cid",cid);

        final CourseData courseData = (CourseData)intent.getSerializableExtra("course");
        saveCourseData = courseData;

        final EditText titleEditText = findViewById(R.id.writeTitle);
        final EditText contentEditText = findViewById(R.id.writeContent);
        contentEditText.setHorizontallyScrolling(false);

        sendPostData = findViewById(R.id.sendPostData);

        sendPostData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String title_value = titleEditText.getText().toString();
                final String content_value =  contentEditText.getText().toString();
                if(title_value.equals("")){
                    Toast.makeText(getApplicationContext(), "제목을 작성해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if(content_value.equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 작성해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                new Thread() {
                    public void run() {
                        Log.d("title,value",title_value+content_value);
                        select_doProcess(cid,title_value,content_value,user_email);

                    }
                }.start();
                Toast.makeText(getApplicationContext(), "작성완료!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(WriteReviewActivity.this, CourseActivity.class);
                intent.putExtra("course",courseData);
                startActivity(intent);
                finish();
            }
        });
    }

    private void select_doProcess(String cid, String title, String content, String user_email) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://15.164.80.191:3000/create_review");
        ArrayList<NameValuePair> nameValues =
                new ArrayList<NameValuePair>();
        cid = cid.replace(" ","_");
        try {
            //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
            nameValues.add(new BasicNameValuePair(
                    "id", URLDecoder.decode(cid, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "title", URLDecoder.decode(title, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "content", URLDecoder.decode(content, "UTF-8")));
            nameValues.add(new BasicNameValuePair(
                    "user_email", URLDecoder.decode(user_email, "UTF-8")));




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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,ReadReviewActivity.class);
        intent.putExtra("cid",saveCid);
        intent.putExtra("course",saveCourseData);
        startActivity(intent);
    }
}
