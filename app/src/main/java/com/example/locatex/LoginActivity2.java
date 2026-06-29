package com.example.locatex;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class LoginActivity2 extends AppCompatActivity {
//    Class Creation
    LottieAnimationView lottielogin;
    EditText etLoginUsername,etLoginPassword;
    CheckBox cbLoginShowHidePassword;
    TextView tvForgotPassword;
    Button btnLoginLogin;
    Button btnLoginNew;
    SharedPreferences Preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Preferences = getDefaultSharedPreferences(LoginActivity2.this);
        editor = Preferences.edit();

        if (Preferences.getBoolean("isLogin",false))
        {
            Intent I = new Intent(LoginActivity2.this,HomepageActivity.class);
            startActivity(I);
            finish();
        }

        lottielogin=findViewById(R.id.lottielogin);
        lottielogin.setRepeatCount(LottieDrawable.INFINITE);
        lottielogin.playAnimation();

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        cbLoginShowHidePassword = findViewById(R.id.cbShowHidePassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginNew = findViewById(R.id.btnLoginNew);

        cbLoginShowHidePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                 if (isChecked){
                     etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                 }
                 else
                 {
                     etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                 }
            }
        });

        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoginUsername.getText().toString().isEmpty())
                {
                    etLoginUsername.setError("Please Enter Your Username");

                }
                else if (etLoginUsername.getText().toString().length()<8)
                {
                    etLoginUsername.setError("Username Must be more than 8");

                }
                else if (etLoginPassword.getText().toString().isEmpty())
                {
                    etLoginPassword.setError("Please Enter Your Password");

                }
                else if (etLoginPassword.getText().toString().length()<8)
                {
                    etLoginPassword.setError("Password Must be more than 8" );

                }
                else if (!etLoginUsername.getText().toString().matches(
                        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^_&+=!]).{8,}$"))
                {
                    etLoginUsername.setError
                            ("Username must contain Uppercase, Lowercase, Number and Special Character");
                }
                else if (!etLoginPassword.getText().toString().matches(
                        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^_&+=!]).{8,}$"))
                {
                    etLoginPassword.setError
                            ("Password must contain Uppercase, Lowercase, Number and Special Character");
                }
                else
                {
                    Toast.makeText(LoginActivity2.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(LoginActivity2.this,HomepageActivity.class);
                    editor.putBoolean("isLogin",true).commit();
                    startActivity(I);
                    finishAffinity();
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(LoginActivity2.this,ForgotPasswordActivity.class);
                startActivity(I);
                finish();
            }
        });

        btnLoginNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(LoginActivity2.this,RegisterActivity.class);
                startActivity(I);
                finish();
            }
        });
    }
}


