package com.example.fquiz.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.Adapter.QuizAnswerAdapter;
import com.example.fquiz.Admin.DataModuler.Question;
import com.example.fquiz.Admin.DataModuler.UserQuizAnswer;
import com.example.fquiz.Api;
import com.example.fquiz.R;
import com.example.fquiz.User.DataModuler.QuizAnswer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminQuizAnswerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    QuizAnswerAdapter answerAdapter;
    List<UserQuizAnswer> quizAnswerList=new ArrayList<>();
    private ProgressDialog progressDialog;
    String quizId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_quiz_answer_list);

            this.setTitle("Answer List");
            quizId=getIntent().getStringExtra("quizId");

            progressDialog=new ProgressDialog(this);

            recyclerView=findViewById(R.id.quizAnswerListRecyclerviewid);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            answerAdapter=new QuizAnswerAdapter(this,quizAnswerList);
            recyclerView.setAdapter(answerAdapter);





        }


    @Override
    protected void onStart() {
        super.onStart();
        getAllQuestion();
    }
    public void getAllQuestion(){
        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminQuizAnswerListActivity.this);
        String url= Api.mainUrl+"answer/"+quizId;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    quizAnswerList.clear();
                    for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);
                        UserQuizAnswer quizAnswer=new UserQuizAnswer(
                                receive.getString("_id"),
                                receive.getString("userName"),
                                receive.getString("userId"),
                                receive.getString("mark"),
                                receive.getString("timeSpent")

                        );

                        quizAnswerList.add(quizAnswer);
                        answerAdapter.notifyDataSetChanged();
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
                Toast.makeText(AdminQuizAnswerListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}