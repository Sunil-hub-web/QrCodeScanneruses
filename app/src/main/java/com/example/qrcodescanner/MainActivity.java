package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_ScannClass;
    public static TextView showTextView;
    String full_name,user_name,password1,contact_no,status_va,id;
    TextView text_Name,text_MobileNo;
    ImageView img_Logout,img_ChangePassword;

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
        id = sessionManager.getSubcatId();

        img_ChangePassword = findViewById(R.id.img_ChangePassword);
        img_Logout = findViewById(R.id.img_Logout);
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

        img_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,ChangePassword.class));
            }
        });

        img_Logout.setOnClickListener(view -> logOut());
    }

    public void logOut(){

        //Show Your Another AlertDialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.logoutdialog);
        dialog.setCancelable(false);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        TextView textView = dialog.findViewById(R.id.editText);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                   /* System.exit(1);
                    finish();*/

                sessionManager.logoutUser();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}