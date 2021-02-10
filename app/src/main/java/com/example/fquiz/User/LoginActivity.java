package com.example.fquiz.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.AdminMainActivity;
import com.example.fquiz.Api;
import com.example.fquiz.MainActivity;
import com.example.fquiz.R;
import com.example.fquiz.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdittext,passwordEdittext;
    private Button loginButton;
    private TextView registerLink;

    private  String email,password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog=new ProgressDialog(this);

        emailEdittext=findViewById(R.id.login_emailEdittextid);
        passwordEdittext=findViewById(R.id.login_PasswordEdittextid);
        loginButton=findViewById(R.id.login_LoginButtonid);
        registerLink=findViewById(R.id.registeractivityLink);




        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailEdittext.getText().toString();
                password=passwordEdittext.getText().toString();



                if(email.isEmpty()){
                    emailEdittext.setError("Please Enter Your Email");
                    emailEdittext.requestFocus();
                }else if(password.isEmpty()){
                    passwordEdittext.setError("Please Enter Your Password");
                    passwordEdittext.requestFocus();
                }else{
                    if(email.equals("admin") && password.equals("244739"))
                    {
                        Users users=new Users(LoginActivity.this);
                        users.saveAdminInfo("admin","244739");
                        startActivity(new Intent(LoginActivity.this,AdminMainActivity.class));
                        finish();
                    }else{
                        loginUser();
                    }

                }

            }
        });






    }


    @Override
    protected void onStart() {
        super.onStart();

        Users users=new Users(LoginActivity.this);
        String usertype=users.getUserType();

        if(!usertype.isEmpty()){
            if(usertype.equals("admin")){
                if(!users.getAdminEmail().equals("")){
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    finish();
                }
            }else{
              String email=users.getUserEmail();
              String className= users.getUserClass();

              if(!email.equals("") && !className.equals("")){
                  startActivity(new Intent(LoginActivity.this, MainActivity.class));
                  finish();

              }

            }
        }




    }

    private void loginUser() {

        progressDialog.setMessage("Logging User");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
        String url= Api.userUrl+"login";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);

                    String status=jsonObject.getString("status");
                    if(status.equals("done")){

                        Users users=new Users(LoginActivity.this);
                        users.saveUserInfo(
                                jsonObject.getString("_id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("className")
                        );
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                        Toast.makeText(LoginActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(LoginActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {
                Map<String, String>  parms=new HashMap<String, String>();
               parms.put("email",email);
                parms.put("password",password);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);
    }
}