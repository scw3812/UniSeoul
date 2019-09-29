package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        TextView textView = (TextView)findViewById(R.id.license_textview);
        textView.setText(Html.fromHtml(getString(R.string.license)));
    }
}
