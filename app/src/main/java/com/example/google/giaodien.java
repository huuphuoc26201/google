package com.example.google;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class giaodien extends AppCompatActivity {

    private static int Splash_Screen=5000;

    Animation Top,Bot;
    private ImageView image;
    private TextView logo,slogan;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_giaodien);
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        Top = AnimationUtils.loadAnimation(this, R.drawable.top);
        Bot = AnimationUtils.loadAnimation(this, R.drawable.bot);
//Set animation to elements
        image.setAnimation(Top);
        logo.setAnimation(Bot);
        slogan.setAnimation(Bot);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(giaodien.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        },Splash_Screen);
    }
}