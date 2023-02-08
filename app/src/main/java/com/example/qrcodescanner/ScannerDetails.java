package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScannerDetails extends AppCompatActivity {

    Button btn_UseMe;
    String scannerApi = "https://smeconsulting.in/sw/Home/scanqr", qrcode_id, main_guest_name,
            main_guest_phone, generate_no, use_date, status_va, QrcodeNo, contact_no, user_name, id,
            full_name, statuesApi = "https://smeconsulting.in/sw/Home/updateqr", room_no, name,
            dateMyFormat,currentDate;

    TextView qrName, qrMobileNo, qrDate, qrStatues, roomno, qrcodefor,txtstatus;
    ImageView img_back;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_details);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sessionManager = new SessionManager(ScannerDetails.this);

        btn_UseMe = findViewById(R.id.btn_UseMe);
        txtstatus = findViewById(R.id.status);
        qrName = findViewById(R.id.qrName);
        qrMobileNo = findViewById(R.id.qrMobileNo);
        qrDate = findViewById(R.id.qrDate);
        qrStatues = findViewById(R.id.qrStatues);
        roomno = findViewById(R.id.roomno);
        qrcodefor = findViewById(R.id.qrcodefor);
        img_back = findViewById(R.id.img_back);

        QrcodeNo = getIntent().getStringExtra("QrcodeNo");

        id = sessionManager.getSubcatId();

        scannerDetails(QrcodeNo);

        btn_UseMe.setOnClickListener(view -> {

            if (qrStatues.getText().toString().trim().equals("New")) {

                updateStatues(id, "0", generate_no);

            }


        });

        img_back.setOnClickListener(view -> {

            startActivity(new Intent(ScannerDetails.this,LoginActivity.class));
        });
    }

    public void scannerDetails(String scannerId) {

        ProgressDialog progressDialog = new ProgressDialog(ScannerDetails.this);
        progressDialog.setMessage("scanner Details Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, scannerApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");

                    if (status.equals("200")) {

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_array = jsonObject_message.getString("status");

                        JSONArray jsonArray = new JSONArray(status_array);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject_data = jsonArray.getJSONObject(0);

                            qrcode_id = jsonObject_data.getString("qrcode_id");
                            room_no = jsonObject_data.getString("room_no");
                            main_guest_name = jsonObject_data.getString("main_guest_name");
                            main_guest_phone = jsonObject_data.getString("main_guest_phone");
                            generate_no = jsonObject_data.getString("generate_no");
                            use_date = jsonObject_data.getString("use_date");
                            name = jsonObject_data.getString("name");
                            status_va = jsonObject_data.getString("status");

                            //qrcodeNo.setText(generate_no);
                            qrName.setText(main_guest_name);
                            qrMobileNo.setText(main_guest_phone);
                            roomno.setText(room_no);
                            qrcodefor.setText(name);

                            use_date = use_date.replace(" 00:00:00", "");


                            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                Date dateFromUser = fromUser.parse(use_date); // Parse it to the exisitng date pattern and return Date type
                                dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
                                qrDate.setText(dateMyFormat);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                            if (status_va.equals("0")) {

                                qrStatues.setText("Already used");
                                txtstatus.setText("Already used");
                                qrStatues.setTextColor(Color.RED);
                                startActivity(new Intent(ScannerDetails.this,ErrorMessageShow.class));
                                btn_UseMe.setEnabled(false);

                              /*  if(dateMyFormat.equals(currentDate)){

                                    qrStatues.setText("New");
                                    qrStatues.setTextColor(Color.GREEN);

                                }else{

                                    startActivity(new Intent(ScannerDetails.this,ErrorMessageShow.class));
                                    btn_UseMe.setEnabled(false);
                                }*/

                            } else {

                                txtstatus.setText("not used");

                                if(dateMyFormat.equals(currentDate)){

                                    qrStatues.setText("New");
                                    qrStatues.setTextColor(Color.GREEN);
                                    btn_UseMe.setEnabled(true);

                                }else{

                                   // startActivity(new Intent(ScannerDetails.this,ErrorMessageShow.class));
                                    qrStatues.setText("Invalide Date");
                                    qrStatues.setTextColor(Color.GREEN);
                                    btn_UseMe.setEnabled(false);

                                    invalideDate_Dialog();
                                }
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(ScannerDetails.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("qrcode", scannerId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ScannerDetails.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void updateStatues(String updated_by, String status, String qrcode) {

        ProgressDialog progressDialog = new ProgressDialog(ScannerDetails.this);
        progressDialog.setMessage("scanner Details Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, statuesApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");

                    JSONObject jsonObject_message = new JSONObject(messages);
                    String responsecode = jsonObject_message.getString("responsecode");
                    String status_array = jsonObject_message.getString("status");

                    if (status.equals("200")) {

                        startActivity(new Intent(ScannerDetails.this, SuccessFullyScreen.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(ScannerDetails.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("updated_by", updated_by);
                params.put("status", status);
                params.put("qrcode", qrcode);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ScannerDetails.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    public void invalideDate_Dialog(){

        Dialog dialog = new Dialog(ScannerDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.invalidedatedialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btn_No = dialog.findViewById(R.id.btn_No);

        btn_No.setOnClickListener(view -> {

            dialog.dismiss();
        });

        dialog.show();

    }
}