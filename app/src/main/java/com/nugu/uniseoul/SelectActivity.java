package com.nugu.uniseoul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SelectActivity extends AppCompatActivity {


    private Button info_btn;
    private Button vol_btn;
    private TextView auth_id_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //String user_name = auth.getCurrentUser().getDisplayName().toString();


        Intent intent = getIntent();
        String user_name = (String) intent.getSerializableExtra("user_name");
        String user_email = (String) intent.getSerializableExtra("user_email");


        auth_id_textView = (TextView)findViewById(R.id.auth_id_textView);
        info_btn = (Button)findViewById(R.id.info_btn);
        vol_btn  = (Button)findViewById(R.id.vol_btn);

        System.out.println(user_name + "\t" + user_email);

        auth_id_textView.setText(user_name + " 님 반갑습니다.");

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_email",user_email);
                startActivity(intent);
            }
        });


        vol_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this,VolActivity.class);
                intent.putExtra("user_name",user_name);
                intent.putExtra("user_email",user_email);
                startActivity(intent);
            }
        });
    }
}
