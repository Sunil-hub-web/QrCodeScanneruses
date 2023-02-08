package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btn_Signin;
    private UiModeManager uiModeManager;
    EditText edit_MobileNo,edit_Password;
    SessionManager sessionManager;

    String loginApi = "https://smeconsulting.in/sw/Home/Applogin",full_name,user_name,password1,contact_no,status_va,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        btn_Signin = findViewById(R.id.btn_Signin);
        edit_MobileNo = findViewById(R.id.edit_MobileNo);
        edit_Password = findViewById(R.id.edit_Password);

        sessionManager = new SessionManager(LoginActivity.this);

        btn_Signin.setOnClickListener(view -> {

            if(edit_MobileNo.getText().toString().trim().equals("")){

                Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();

            }else if(edit_Password.getText().toString().trim().equals("")){

                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();

            }else{

                String str_userName = edit_MobileNo.getText().toString().trim();
                String str_Password = edit_Password.getText().toString().trim();
                userLogin(str_userName,str_Password);

            }

        });
    }

    public void userLogin(String userid,String password){

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Login Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");

                    if(status.equals("200")){

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_array = jsonObject_message.getString("status");

                        JSONArray jsonArray = new JSONArray(status_array);

                        for (int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject_data = jsonArray.getJSONObject(0);

                            id = jsonObject_data.getString("id");
                            full_name = jsonObject_data.getString("full_name");
                            user_name = jsonObject_data.getString("user_name");
                            password1 = jsonObject_data.getString("password");
                            contact_no = jsonObject_data.getString("contact_no");
                            status_va = jsonObject_data.getString("status");

                            sessionManager.setLogin();
                            sessionManager.setUserFullName(full_name);
                            sessionManager.setUserName(user_name);
                            sessionManager.setUserMobileNO(contact_no);
                            sessionManager.setSubcatId(id);


                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        }

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("full_name",full_name);
                        intent.putExtra("user_name",user_name);
                        intent.putExtra("contact_no",contact_no);

                        startActivity(intent);

                    }else{

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_array = jsonObject_message.getString("status");

                        Toast.makeText(LoginActivity.this, status_array, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("username",userid);
                params.put("Password",password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sessionManager.isLogin()){

            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }
}