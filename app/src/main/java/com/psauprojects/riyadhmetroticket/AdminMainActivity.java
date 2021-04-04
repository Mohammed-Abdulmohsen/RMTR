package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import java.util.Locale;

public class AdminMainActivity extends AppCompatActivity {
    Intent intent;

    ImageView imageView_close,imageView_info,imageView_add_station,imageView_add_train;
    String mobile;
    WebView webView_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        intent = getIntent();
        mobile = intent.getExtras().getString("mobile");

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_info = (ImageView) findViewById(R.id.imageView_info);
        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this,AdminAccountActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        imageView_add_station = (ImageView) findViewById(R.id.imageView_add_station);
        imageView_add_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this,AdminAddStationActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        imageView_add_train =(ImageView) findViewById(R.id.imageView_add_train);
        imageView_add_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this,AdminAddTrainActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        webView_home = (WebView) findViewById(R.id.webView_home);
        WebSettings webSettings = webView_home.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(Locale.getDefault().getDisplayLanguage().equals("English")){
            webView_home.loadUrl("http://psauprojects.com/ksatrain/admin_home_en.php");
        }else{
            webView_home.loadUrl("http://psauprojects.com/ksatrain/admin_home_ar.php");
        }
    }
}
