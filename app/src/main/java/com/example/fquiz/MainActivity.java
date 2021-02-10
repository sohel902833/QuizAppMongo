package com.example.fquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.Adapter.QuizPublishedAdapter;
import com.example.fquiz.Admin.DataModuler.PublishQuiz;
import com.example.fquiz.Admin.DataModuler.Quiz;
import com.example.fquiz.User.LoginActivity;
import com.example.fquiz.User.QuizActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String className;
    private RecyclerView recyclerView;
    private List<PublishQuiz> quizList=new ArrayList<>();
    private QuizPublishedAdapter quizAdapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Quiz List");
        progressDialog=new ProgressDialog(this);

        recyclerView=findViewById(R.id.main_QuizListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizAdapter=new QuizPublishedAdapter(this,quizList);
        recyclerView.setAdapter(quizAdapter);



        quizAdapter.setOnItemClickListner(new QuizPublishedAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {



                PublishQuiz currentItem=quizList.get(position);

                if(isQuizComplete(currentItem.getQuizId())){
                    Toast.makeText(MainActivity.this, "You Already Play this quiz", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("quizId",currentItem.getQuizId());
                    intent.putExtra("id",currentItem.getId());
                    intent.putExtra("time",currentItem.getEndTime());
                    startActivity(intent);
                }


            }

            @Override
            public void onPublishQuiz(int position) {

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

        Users users=new Users(MainActivity.this);

        getAllPublishedQuiz(users.getUserClass());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public Boolean isQuizComplete(String quizId){
        Users users=new Users(this);
        String data= users.getUserId()+quizId;
        SharedPreferences sharedPreferences=getSharedPreferences("quizData", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString(data,"");

        if(name.equals("done")){
            return true;
        }else{
            return  false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.main_Logout_MenuButtonid){
            Users users=new Users(MainActivity.this);
            users.saveUserInfo("","","","");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void getAllPublishedQuiz(String className) {

        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        String url= Api.mainUrl+"publish/quiz/"+className;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    quizList.clear();
                    for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);
                        PublishQuiz quiz=new PublishQuiz(
                                receive.getString("_id"),
                                receive.getString("quizId"),
                                receive.getString("quizName"),
                                receive.getString("image"),
                                receive.getString("totalQuestion"),
                                receive.getString("time"),
                                receive.getString("endTime"),
                                receive.getString("className")

                        );

                        quizList.add(quiz);
                        quizAdapter.notifyDataSetChanged();
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
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}