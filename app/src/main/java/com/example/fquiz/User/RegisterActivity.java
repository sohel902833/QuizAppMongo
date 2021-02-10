package com.example.fquiz.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.Adapter.SpinnerSampleAdapter;
import com.example.fquiz.Admin.AdminMainActivity;
import com.example.fquiz.Api;
import com.example.fquiz.MainActivity;
import com.example.fquiz.R;
import com.example.fquiz.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdittext,passwordEdittext,nameEdittext;
    private Button regsterButton;
    private TextView loginlink;
    private Spinner classSpinner;
    private  String email,password,name,selectedClass;
    private SpinnerSampleAdapter classAdapter;
    String[] classes={"Class-6","Class-7","Class-8","Class-9","Class-10"};



    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog=new ProgressDialog(this);

        emailEdittext=findViewById(R.id.register_emailEdittextid);
        passwordEdittext=findViewById(R.id.register_PasswordEdittextid);
        nameEdittext=findViewById(R.id.register_nameEidttextid);
        regsterButton=findViewById(R.id.register_RegisterButtonid);
        loginlink=findViewById(R.id.loginactivityLink);
        classSpinner=findViewById(R.id.register_ClassSpinnerid);

        classAdapter=new SpinnerSampleAdapter(this,classes);
        classSpinner.setAdapter(classAdapter);

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClass=classes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        regsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailEdittext.getText().toString();
                password=passwordEdittext.getText().toString();
                name=nameEdittext.getText().toString();

                if(name.isEmpty()){
                    emailEdittext.setError("Please Enter Your Name");
                    emailEdittext.requestFocus();
                }else if(email.isEmpty()){
                    emailEdittext.setError("Please Enter Your Email");
                    emailEdittext.requestFocus();
                }else if(password.isEmpty()){
                    passwordEdittext.setError("Please Enter Your Password");
                    passwordEdittext.requestFocus();
                }else if(selectedClass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Select Your Class", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser();
                }

            }
        });


    }

    private void registerUser() {


        progressDialog.setMessage("Saving Category");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
        String url= Api.userUrl+"register";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);

                    String status=jsonObject.getString("status");
                    if(status.equals("done")){

                        Users users=new Users(RegisterActivity.this);
                        users.saveUserInfo(
                                jsonObject.getString("_id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("className")
                        );
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();

                        Toast.makeText(RegisterActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(RegisterActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                 } catch (JSONException e) {
                    progressDialog.dismiss();
               }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {
                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("name",name);
                parms.put("className",selectedClass);
                parms.put("email",email);
                parms.put("password",password);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);

    }
}