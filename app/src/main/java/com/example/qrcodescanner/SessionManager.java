package com.example.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.window.SplashScreen;

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;

    Context context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME = "sharedcheckLogin";
    private static final String SUBCAT_ID = "subcatid";
    private static final String USER_FULLNAME = "userfullname";
    private static final String USER_NAME = "username";
    private static final String USER_MOBILENO = "usermobile";
    private static final String IS_LOGIN="islogin";
    private static final String IS_Status="isstatues";


    public SessionManager(Context context){

        this.context =  context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedprefernce.edit();
    }

    public void setSubcatId(String id ){

        editor.putString(SUBCAT_ID,id);
        editor.commit();

    }
    public String getSubcatId(){

        return  sharedprefernce.getString(SUBCAT_ID,"DEFAULT");
    }

    public void setUserMobileNO(String mobileNO){

        editor.putString(USER_MOBILENO,mobileNO);
        editor.commit();
    }

    public String getUserFullName(){

        return  sharedprefernce.getString(USER_FULLNAME,"DEFAULT");
    }

    public void setUserFullName(String fullname){

        editor.putString(USER_FULLNAME,fullname);
        editor.commit();
    }

    public String getUserName(){

        return  sharedprefernce.getString(USER_NAME,"DEFAULT");
    }

    public void setUserName(String name){

        editor.putString(USER_NAME,name);
        editor.commit();
    }

    public String getUserMobileno(){

        return sharedprefernce.getString(USER_MOBILENO,"DEFAULT");

    }

    public Boolean isLogin(){
        return sharedprefernce.getBoolean(IS_LOGIN, false);

    }
    public void setLogin(){

        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);





    }
}
