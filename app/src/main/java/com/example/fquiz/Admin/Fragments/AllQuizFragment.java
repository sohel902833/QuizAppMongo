package com.example.fquiz.Admin.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.Adapter.QuizAdapter;
import com.example.fquiz.Admin.Adapter.SpinnerSampleAdapter;
import com.example.fquiz.Admin.AdminMainActivity;
import com.example.fquiz.Admin.AdminQuestionActivity;
import com.example.fquiz.Admin.AdminQuizActivity;
import com.example.fquiz.Admin.DataModuler.Category;
import com.example.fquiz.Admin.DataModuler.Quiz;
import com.example.fquiz.Api;
import com.example.fquiz.MainActivity;
import com.example.fquiz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AllQuizFragment extends Fragment {

    String categoryId;
    public AllQuizFragment(String categoryId) {
        // Required empty public constructor
        this.categoryId=categoryId;
    }
    private RecyclerView recyclerView;
    private FloatingActionButton quizAddButton;

    private EditText quizNameEdittext;
    private String quizName;
    private Button addButton;
    private ProgressDialog progressDialog;
    QuizAdapter quizAdapter;



    private List<Quiz> quizList=new ArrayList<>();



    //<-----------publish quiz diolouge------------->

        private Spinner timeSpinner,classSpinner;
        private SpinnerSampleAdapter timeAdapter;
        private SpinnerSampleAdapter classAdapter;
        private  Button publishQuizButton;

        String[] classes={"Class-6","Class-7","Class-8","Class-9","Class-10"};
        String [] times={"5min","10min","15min","20min","25min","30min"};

        String selectedClass,selectedTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_all_quiz, container, false);

        progressDialog=new ProgressDialog(getContext());
        recyclerView=view.findViewById(R.id.allQuizRecyclerviewid);
        quizAddButton=view.findViewById(R.id.addQuizFloatingButtonid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

         quizAdapter=new QuizAdapter(getContext(),quizList);
        recyclerView.setAdapter(quizAdapter);

        timeAdapter=new SpinnerSampleAdapter(getContext(),times);
        classAdapter=new SpinnerSampleAdapter(getContext(),classes);



        quizAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewQuizDiolouge();
            }
        });



        quizAdapter.setOnItemClickListner(new QuizAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {

                Quiz currentItem=quizList.get(position);

                Intent intent=new Intent(getContext(), AdminQuestionActivity.class);
                intent.putExtra("quizId",currentItem.getId());
                startActivity(intent);


            }

            @Override
            public void onPublishQuiz(int position) {
                showPublishQuizDiolouge(position);
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
        getAllQuiz();
    }


    public void showPublishQuizDiolouge(int position)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.publish_quiz_diolouge,null);
        builder.setView(view);

        timeSpinner=view.findViewById(R.id.publishQuiz_QuizTimeSpinnerid);
        classSpinner=view.findViewById(R.id.publishQuizClassSpinnerid);
        publishQuizButton=view.findViewById(R.id.publishQuizButton);

        timeSpinner.setAdapter(timeAdapter);
        classSpinner.setAdapter(classAdapter);


        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime=times[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


        final AlertDialog dialog=builder.create();
        dialog.show();
        publishQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedTime.isEmpty()){
                    Toast.makeText(getContext(), "Choose The Time", Toast.LENGTH_SHORT).show();
                }else if(selectedClass.isEmpty()){
                    Toast.makeText(getContext(), "Choose The Class", Toast.LENGTH_SHORT).show();
                }else{
                    publishQuiz(dialog,position);
                }

            }


        });
    }

    public int getTimeInMillisecond(String time){
        int t=0;

        if(time.equals("5min"))
            t= 300000;
        else if(time.equals("10min"))
            t= 600000;
        else if(time.equals("15min"))
            t= 900000;
        else if(time.equals("20min"))
            t= 1200000;
        else if(time.equals("25min"))
            t= 1500000;
        else if(time.equals("30min"))
            t= 1800000;

       return  t;
    }

    private void publishQuiz(Dialog dialog,int position) {
        progressDialog.setMessage("Publishing This Quiz");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url= Api.mainUrl+"publish";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Intent intent=new Intent(getContext(), AdminQuizActivity.class);
                    intent.putExtra("categoryId",categoryId);
                    startActivity(intent);

                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(getContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {
                Quiz currentItem=quizList.get(position);

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("quizName",currentItem.getQuizName());
                parms.put("quizId",currentItem.getId());
                parms.put("image",currentItem.getImage());
                parms.put("time", String.valueOf(System.currentTimeMillis()));
                parms.put("totalQuestion",currentItem.getTotalQuestions());
                parms.put("endTime", String.valueOf(getTimeInMillisecond(selectedTime)));
                parms.put("className",selectedClass);
                return  parms;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void saveNewQuizDiolouge(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.add_quiz_diolouge_layout,null);
        builder.setView(view);
        quizNameEdittext=view.findViewById(R.id.quiz_diolouge_QuizNameEdittext);
        addButton=view.findViewById(R.id.quiz_diolouge_SaveButtonid);


        final AlertDialog dialog=builder.create();
        dialog.show();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizName=quizNameEdittext.getText().toString();
                if(quizName.isEmpty()){
                    quizNameEdittext.setError("Enter Quiz Name");
                    quizNameEdittext.requestFocus();
                }else{
                    saveNewQuiz(dialog);
                }
            }
        });
    }


    private void saveNewQuiz(AlertDialog dialog) {
        progressDialog.setMessage("Saving Quiz");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url= Api.mainUrl;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(getContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {
                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("categoryId",categoryId);
                parms.put("quizName",quizName);
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
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url=Api.mainUrl+categoryId;

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




                        Quiz quiz=new Quiz(
                                receive.getString("_id"),
                                receive.getString("quizName"),
                                receive.getString("image"),
                                receive.getString("totalQuestions")
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