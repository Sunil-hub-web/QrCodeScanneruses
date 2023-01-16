package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SuccessFullyScreen extends AppCompatActivity {

    Button btn_backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_success_fully_screen);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_backToHome = findViewById(R.id.btn_backToHome);

        btn_backToHome.setOnClickListener(view -> {

            startActivity(new Intent(SuccessFullyScreen.this,MainActivity.class));
        });


    }
}