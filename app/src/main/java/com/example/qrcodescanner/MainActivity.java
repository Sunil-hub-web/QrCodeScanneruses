package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_ScannClass;
    public static TextView showTextView;
    String full_name,user_name,password1,contact_no,status_va,id;
    TextView text_Name,text_MobileNo;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sessionManager = new SessionManager(MainActivity.this);

        full_name = sessionManager.getUserFullName();
        user_name = sessionManager.getUserName();
        contact_no = sessionManager.getUserMobileno();

        btn_ScannClass = findViewById(R.id.btn_ScannClass);
        showTextView = findViewById(R.id.showTextView);
        text_Name = findViewById(R.id.text_Name);
        text_MobileNo = findViewById(R.id.text_MobileNo);

        text_MobileNo.setText(contact_no);
        text_Name.setText(full_name);

        btn_ScannClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,ScannerClass.class));

            }
        });
    }
}