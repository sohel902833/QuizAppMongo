package com.example.fquiz.Admin.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.Adapter.QuizPublishedAdapter;
import com.example.fquiz.Admin.AdminQuestionActivity;
import com.example.fquiz.Admin.AdminQuizAnswerListActivity;
import com.example.fquiz.Admin.DataModuler.PublishQuiz;
import com.example.fquiz.Admin.DataModuler.Question;
import com.example.fquiz.Api;
import com.example.fquiz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PublishQuizFragment extends Fragment {

    public PublishQuizFragment() {

    }


    private List<PublishQuiz> quizList=new ArrayList<>();
    private RecyclerView recyclerView;
    private QuizPublishedAdapter quizAdapter;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_publish_quiz, container, false);

       progressDialog=new ProgressDialog(getContext());
       recyclerView=view.findViewById(R.id.publishQuizRecyclerViewid);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       quizAdapter=new QuizPublishedAdapter(getContext(),quizList);
       recyclerView.setAdapter(quizAdapter);


       quizAdapter.setOnItemClickListner(new QuizPublishedAdapter.OnItemClickListner() {
           @Override
           public void onItemClick(int position) {
               PublishQuiz currentItem=quizList.get(position);
               Intent intent=new Intent(getContext(), AdminQuizAnswerListActivity.class);
               intent.putExtra("quizId",currentItem.getId());

               startActivity(intent);
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




       return  view;
    }


    @Override
    public void onStart() {
        super.onStart();

        getAllPublishedQuiz();




    }

    private void getAllPublishedQuiz() {

        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url= Api.mainUrl+"publish/quiz/";

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
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}