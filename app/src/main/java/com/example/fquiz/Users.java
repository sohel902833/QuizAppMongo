package com.example.fquiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Users {
    Activity activity;

    public Users(Activity activity) {
        this.activity = activity;
    }

    public void  saveUserInfo(String id,String name,String email,String className){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("className",className);
        editor.putString("usertype","local");
        editor.commit();
    }
    public void  saveAdminInfo(String email,String password){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("aemail",email);
        editor.putString("usertype","admin");
        editor.commit();
    }
    public String getUserId(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String id=sharedPreferences.getString("id","");
        return  id;
    }  public String getUserName(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("name","");
        return  name;
    }
    public String getUserEmail(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");
        return  email;
    }
  public String getUserClass(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String className=sharedPreferences.getString("className","");
        return  className;
    }
  public String getUserType(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String usertype=sharedPreferences.getString("usertype","");
        return  usertype;
    }

  public String  getAdminEmail(){
      SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
      String email=sharedPreferences.getString("aemail","");
      return  email;

  }




}
