package com.example.locatex;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class MainActivity extends AppCompatActivity {
    //    creation class
    ImageView ivSplashLogo;
    TextView tvSplashTitle;
    LottieAnimationView lottiesplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locatex_activity);

        ivSplashLogo = findViewById(R.id.ivSplashLogo);
        tvSplashTitle = findViewById(R.id.tvSplashTitle);

        lottiesplash=findViewById(R.id.lottiesplash);
        lottiesplash.setRepeatCount(LottieDrawable.INFINITE);
        lottiesplash.playAnimation();

        Handler H = new Handler();
        H.postDelayed(() -> {
            Intent I = new Intent(MainActivity.this,LoginActivity2.class);
            startActivity(I);
            finishAffinity();
        },3000);
    }
}