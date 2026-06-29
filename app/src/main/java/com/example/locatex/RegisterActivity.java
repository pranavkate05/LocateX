package com.example.locatex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;


public class RegisterActivity extends AppCompatActivity {
    LottieAnimationView lottieRegister;
    EditText etRegisterName, etRegisterUsername, etRegisterEmailId, etRegisterMobileNo, etRegisterPassword, etRegisterRetypepassword;
    CheckBox cbShowHideRegisterPassword;
    Button btnRegisterNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        lottieRegister = findViewById(R.id.lottieRegister);
        lottieRegister.setRepeatCount(LottieDrawable.INFINITE);
        lottieRegister.playAnimation();


        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterEmailId = findViewById(R.id.etRegisterEmailId);
        etRegisterMobileNo = findViewById(R.id.etRegisterMobileNo);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterRetypepassword = findViewById(R.id.etRegisterRetypePassword);
        cbShowHideRegisterPassword = findViewById(R.id.cbShowHideRegisterPassword);
        btnRegisterNew = findViewById(R.id.btnRegisterNew);

        cbShowHideRegisterPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    etRegisterRetypepassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etRegisterRetypepassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnRegisterNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etRegisterName.getText().toString().isEmpty()) {
                    etRegisterName.setError("Please Enter Your Full Name.");
                } else if (etRegisterUsername.getText().toString().isEmpty()) {
                    etRegisterUsername.setError("Please Enter Your Username");

                } else if (etRegisterUsername.getText().toString().length() < 8) {
                    etRegisterUsername.setError("Username Must be more than 8");

                } else if (!etRegisterUsername.getText().toString().matches(
                        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^_&+=!]).{8,}$")) {
                    etRegisterUsername.setError
                            ("Username must contain Uppercase, Lowercase, Number and Special Character");
                } else if (etRegisterEmailId.getText().toString().isEmpty()) {
                    etRegisterEmailId.setError("Please Enter Your Email Address.");
                } else if (!etRegisterEmailId.getText().toString().contains("@") || !etRegisterEmailId.getText().toString().contains(".com")) {
                    etRegisterEmailId.setError("Please enter Valid Email Id");
                } else if (etRegisterMobileNo.getText().toString().isEmpty()) {
                    etRegisterMobileNo.setError("Please Enter Your Mobile No.");
                } else if (etRegisterMobileNo.getText().toString().length() != 10) {
                    etRegisterMobileNo.setError("Mobile No. Length Must be 10");
                } else if (etRegisterPassword.getText().toString().isEmpty()) {
                    etRegisterPassword.setError("Please Enter Your Password");
                } else if (etRegisterPassword.getText().toString().length() < 8) {
                    etRegisterPassword.setError("Password must be more than 8");
                } else if (!etRegisterPassword.getText().toString().matches(
                        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^_&+=!]).{8,}$")) {
                    etRegisterPassword.setError("Password must contain Uppercase, Lowercase, Number and Special Character");
                } else if (etRegisterRetypepassword.getText().toString().isEmpty()) {
                    etRegisterRetypepassword.setError("Please Enter Your Password");
                } else if (etRegisterRetypepassword.getText().toString().length() < 8) {
                    etRegisterRetypepassword.setError("Password must be more than 8");
                } else if (!etRegisterRetypepassword.getText().toString().matches(
                        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^_&+=!]).{8,}$")) {
                    etRegisterRetypepassword.setError("Password must contain Uppercase, Lowercase, Number and Special Character");
                } else if (!etRegisterPassword.getText().toString().equals(etRegisterRetypepassword.getText().toString())) {
                    etRegisterRetypepassword.setError("Password and Confirm Password not match");
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent I = new Intent(RegisterActivity.this,LoginActivity2.class);
        startActivity(I);

    }
}





