package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ErrorMessageShow extends AppCompatActivity {

    Button btn_backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_message_show);

        btn_backToHome = findViewById(R.id.btn_backToHome);

        btn_backToHome.setOnClickListener(view -> {

            startActivity(new Intent(ErrorMessageShow.this,MainActivity.class));
        });
    }
}