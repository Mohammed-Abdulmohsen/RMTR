package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class PassMainActivity extends AppCompatActivity {

    Intent intent;
    String mobile;
    WebView webView_home;
    ImageView imageView_close,imageView_info,imageView_stations,imageView_ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_main);

        intent = getIntent();
        mobile = intent.getExtras().getString("mobile");

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassMainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_info = (ImageView) findViewById(R.id.imageView_info);
        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassMainActivity.this,PassAccountActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        imageView_stations = (ImageView) findViewById(R.id.imageView_stations);
        imageView_stations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassMainActivity.this,PassViewStationsActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        imageView_ticket = (ImageView) findViewById(R.id.imageView_ticket);
        imageView_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PassMainActivity.this,PassTicketsActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        webView_home = (WebView) findViewById(R.id.webView_home);
        WebSettings webSettings = webView_home.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView_home.loadUrl("http://psauprojects.com/ksatrain/pass_home_en.php");

    }
}
