package com.example.fquiz.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.Adapter.CategoryAdapter;
import com.example.fquiz.Admin.Adapter.TabLayoutAdapter;
import com.example.fquiz.Admin.DataModuler.Category;
import com.example.fquiz.Api;
import com.example.fquiz.MainActivity;
import com.example.fquiz.R;
import com.example.fquiz.User.LoginActivity;
import com.example.fquiz.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private EditText categoryEdittext;
    private Button addCategoryButton;
    private  String category;
    private ProgressDialog progressDialog;
    private List<Category> categoryList=new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        this.setTitle("Category List");

        progressDialog=new ProgressDialog(this);


        addButton=findViewById(R.id.addCategoryFloatingActionButton);
        recyclerView=findViewById(R.id.categoryListRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryAdapter=new CategoryAdapter(this,categoryList);
        recyclerView.setAdapter(categoryAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentDiolouge();
            }
        });




        categoryAdapter.setOnItemClickListner(new CategoryAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(AdminMainActivity.this,AdminQuizActivity.class);
                intent.putExtra("categoryId",categoryList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onUpdate(int position) {

            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();

        getAllQuiz();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.main_Logout_MenuButtonid){
            Users users=new Users(AdminMainActivity.this);
            users.saveUserInfo("","","","");
            startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void addStudentDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminMainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_category_diolouge_layout,null);
        builder.setView(view);
        categoryEdittext=view.findViewById(R.id.category_diolouge_CategoryNameEditteXt);
        addCategoryButton=view.findViewById(R.id.category_diolouge_SaveButtonId);


        final AlertDialog dialog=builder.create();
        dialog.show();
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                category=categoryEdittext.getText().toString();
                if(category.isEmpty()){
                    categoryEdittext.setError("Enter Category");
                    categoryEdittext.requestFocus();
                }else{
                    saveStudent(dialog);
                }
            }
        });
    }

    private void saveStudent(AlertDialog dialog) {

        progressDialog.setMessage("Saving Category");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(AdminMainActivity.this);
        String url= Api.mainUrl+"cat";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(AdminMainActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    onStart();
                    dialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(AdminMainActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {
                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("category",category);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getAllQuiz(){
        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminMainActivity.this);
        String url=Api.mainUrl+"cat";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    categoryList.clear();
                    for(int i=0; i<array.length(); i++){

                        JSONObject receive=array.getJSONObject(i);




                        Category category=new Category(
                                receive.getString("_id"),
                                receive.getString("categoryName")

                        );

                        categoryList.add(category);
                        categoryAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AdminMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}