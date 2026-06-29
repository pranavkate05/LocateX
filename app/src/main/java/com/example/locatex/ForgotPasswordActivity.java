package com.example.locatex;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class ForgotPasswordActivity extends AppCompatActivity {
    LottieAnimationView lottieforgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwoed);
        lottieforgot=findViewById(R.id.lottieforgot);
        lottieforgot.setRepeatCount(LottieDrawable.INFINITE);
        lottieforgot.playAnimation();

        Toast.makeText(this, "Forgot Successfully", Toast.LENGTH_SHORT).show();

    }
}