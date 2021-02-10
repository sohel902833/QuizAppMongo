package com.example.fquiz.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.fquiz.Admin.Adapter.QuestionListAdapter;
import com.example.fquiz.Admin.Adapter.SpinnerSampleAdapter;
import com.example.fquiz.Admin.DataModuler.Category;
import com.example.fquiz.Admin.DataModuler.Question;
import com.example.fquiz.Api;
import com.example.fquiz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminQuestionActivity extends AppCompatActivity {

    private  String[] values={"1","2","3","4"};
    private SpinnerSampleAdapter answerspinnerAdapter;
    String quizId;





    //<---------------layouts-------------------->
    private RecyclerView recyclerView;
    private FloatingActionButton questionAddButton;
    //<---------------dialouge layouts-------------------->
    private EditText questionEdittext,option1Edittext,option2Edittext,option3Edittext,option4Edittext,marksEdittext;
    private Spinner answerNumberSpinner;
    private  Button saveQuestionButton;




    //<--------------------variables------------------------->
        private  String question,option1,option2,option3,option4,marks,selectedAnswerNumber;
        private ProgressDialog progressDialog;
      private List<Question> questionList=new ArrayList<>();
      private QuestionListAdapter questionAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_question);

        progressDialog=new ProgressDialog(this);

        quizId=getIntent().getStringExtra("quizId");

        recyclerView=findViewById(R.id.questionListRecyclerViewid);
        questionAddButton=findViewById(R.id.addQuestionFloatingButton);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        answerspinnerAdapter=new SpinnerSampleAdapter(this,values);
        questionAdapter=new QuestionListAdapter(this,questionList);
        recyclerView.setAdapter(questionAdapter);



        questionAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionDiolouge();
            }
        });


        questionAdapter.setOnItemClickListner(new QuestionListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDelete(int position) {
                deleteQuestion(questionList.get(position).getId());
            }

            @Override
            public void onUpdate(int position) {
                showUpdateDiolouge(position);
            }
        });




    }



    @Override
    protected void onStart() {
        super.onStart();
        getAllQuestion();
    }

    public void addQuestionDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminQuestionActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_quesiton_diolouge_layout,null);
        builder.setView(view);

        questionEdittext=view.findViewById(R.id.question_diolouge_QuestionEdittextid);
        option1Edittext=view.findViewById(R.id.question_diolouge_Option1Edittextid);
        option2Edittext=view.findViewById(R.id.question_diolouge_Option2Edittextid);
        option3Edittext=view.findViewById(R.id.question_diolouge_Option3Edittextid);
        option4Edittext=view.findViewById(R.id.question_diolouge_Option4Edittextid);
        marksEdittext=view.findViewById(R.id.question_diolouge_MarksEdittextid);
        answerNumberSpinner=view.findViewById(R.id.question_diolouge_AnswerNumberSpinnerid);
        saveQuestionButton=view.findViewById(R.id.saveQuestionButtonid);

        answerNumberSpinner.setAdapter(answerspinnerAdapter);

        answerNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnswerNumber=values[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final AlertDialog dialog=builder.create();
        dialog.show();
        saveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question=questionEdittext.getText().toString();
                option1=option1Edittext.getText().toString();
                option2=option2Edittext.getText().toString();
                option3=option3Edittext.getText().toString();
                option4=option4Edittext.getText().toString();
                marks=marksEdittext.getText().toString();




                if(question.isEmpty()){
                    questionEdittext.setError("Enter Question");
                    questionEdittext.requestFocus();
                }else if(option1.isEmpty()){
                    option1Edittext.setError("Enter Option 1");
                    option1Edittext.requestFocus();
                }else if(option2.isEmpty()){
                    option2Edittext.setError("Enter Option 2");
                    option2Edittext.requestFocus();
                }else if(option3.isEmpty()){
                    option3Edittext.setError("Enter Option 3");
                    option3Edittext.requestFocus();
                }else if(option4.isEmpty()){
                    option4Edittext.setError("Enter Option 4");
                    option4Edittext.requestFocus();
                }else if(marks.isEmpty()){
                    marksEdittext.setError("Enter Marks");
                    marksEdittext.requestFocus();
                }else if(selectedAnswerNumber.isEmpty()){
                    Toast.makeText(AdminQuestionActivity.this, "Select An Answer Number", Toast.LENGTH_SHORT).show();
                }else{
                    saveQuestion(dialog);
                }
            }
        });
    }


    private void showUpdateDiolouge(int position) {

        Question currentItem=questionList.get(position);

        AlertDialog.Builder builder=new AlertDialog.Builder(AdminQuestionActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_quesiton_diolouge_layout,null);
        builder.setView(view);

        questionEdittext=view.findViewById(R.id.question_diolouge_QuestionEdittextid);
        option1Edittext=view.findViewById(R.id.question_diolouge_Option1Edittextid);
        option2Edittext=view.findViewById(R.id.question_diolouge_Option2Edittextid);
        option3Edittext=view.findViewById(R.id.question_diolouge_Option3Edittextid);
        option4Edittext=view.findViewById(R.id.question_diolouge_Option4Edittextid);
        marksEdittext=view.findViewById(R.id.question_diolouge_MarksEdittextid);
        answerNumberSpinner=view.findViewById(R.id.question_diolouge_AnswerNumberSpinnerid);
        saveQuestionButton=view.findViewById(R.id.saveQuestionButtonid);

        answerNumberSpinner.setAdapter(answerspinnerAdapter);


        questionEdittext.setText(""+currentItem.getQuestion());
        option1Edittext.setText(""+currentItem.getOption1());
        option2Edittext.setText(""+currentItem.getOption2());
        option3Edittext.setText(""+currentItem.getOption3());
        option4Edittext.setText(""+currentItem.getOption4());
        marksEdittext.setText(""+currentItem.getMark());

        int ansNumber= Integer.parseInt(currentItem.getAnswerNr())-1;
        selectedAnswerNumber=currentItem.getAnswerNr();
        answerNumberSpinner.setSelection(ansNumber);




        answerNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnswerNumber=values[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final AlertDialog dialog=builder.create();
        dialog.show();
        saveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question=questionEdittext.getText().toString();
                option1=option1Edittext.getText().toString();
                option2=option2Edittext.getText().toString();
                option3=option3Edittext.getText().toString();
                option4=option4Edittext.getText().toString();
                marks=marksEdittext.getText().toString();




                if(question.isEmpty()){
                    questionEdittext.setError("Enter Question");
                    questionEdittext.requestFocus();
                }else if(option1.isEmpty()){
                    option1Edittext.setError("Enter Option 1");
                    option1Edittext.requestFocus();
                }else if(option2.isEmpty()){
                    option2Edittext.setError("Enter Option 2");
                    option2Edittext.requestFocus();
                }else if(option3.isEmpty()){
                    option3Edittext.setError("Enter Option 3");
                    option3Edittext.requestFocus();
                }else if(option4.isEmpty()){
                    option4Edittext.setError("Enter Option 4");
                    option4Edittext.requestFocus();
                }else if(marks.isEmpty()){
                    marksEdittext.setError("Enter Marks");
                    marksEdittext.requestFocus();
                }else if(selectedAnswerNumber.isEmpty()){
                    Toast.makeText(AdminQuestionActivity.this, "Select An Answer Number", Toast.LENGTH_SHORT).show();
                }else{
                    updateQuestion(dialog,currentItem.getId());
                }
            }
        });

    }



    public void getAllQuestion(){
        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(AdminQuestionActivity.this);
        String url=Api.mainUrl+"question/"+quizId;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("result");
                    questionList.clear();
                    for(int i=0; i<array.length(); i++){
                        JSONObject receive=array.getJSONObject(i);
                        Question question=new Question(
                                receive.getString("_id"),
                                receive.getString("question"),
                                receive.getString("option1"),
                                receive.getString("option2"),
                                receive.getString("option3"),
                                receive.getString("option4"),
                                receive.getString("answerNr"),
                                receive.getString("mark")

                        );

                        questionList.add(question);
                        questionAdapter.notifyDataSetChanged();
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
                Toast.makeText(AdminQuestionActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void deleteQuestion(String qid){
    progressDialog.setMessage("Deleting...");
    progressDialog.setTitle("Please Wait..");
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();
    RequestQueue requestQueue= Volley.newRequestQueue(AdminQuestionActivity.this);
    String url=Api.mainUrl+quizId+"/"+qid;

    StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                progressDialog.dismiss();
                JSONObject jsonObject=new JSONObject(response);
                Toast.makeText(AdminQuestionActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                onStart();
           } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
            Toast.makeText(AdminQuestionActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    });
    requestQueue.add(stringRequest);
}

   public void updateQuestion(Dialog dialog,String questionId){
       progressDialog.setMessage("Updating Question");
       progressDialog.setTitle("Please Wait..");
       progressDialog.setCanceledOnTouchOutside(false);
       progressDialog.show();

       RequestQueue requestQueue= Volley.newRequestQueue(AdminQuestionActivity.this);
       String url= Api.mainUrl+quizId+"/"+questionId;

       StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   progressDialog.dismiss();
                   JSONObject jsonObject=new JSONObject(response);
                   Toast.makeText(AdminQuestionActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
               Toast.makeText(AdminQuestionActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
           }
       }){

           @Override
           protected Map<String, String> getParams()  {
               Map<String, String>  parms=new HashMap<String, String>();
               parms.put("question",question);
               parms.put("option1",option1);
               parms.put("option2",option2);
               parms.put("option3",option3);
               parms.put("option4",option4);
               parms.put("answerNr",selectedAnswerNumber);
               parms.put("mark",marks);
               return  parms;
           }
       };
       requestQueue.add(stringRequest);
    }

   public void saveQuestion(Dialog dialog){
       progressDialog.setMessage("Saving Question");
       progressDialog.setTitle("Please Wait..");
       progressDialog.setCanceledOnTouchOutside(false);
       progressDialog.show();

       RequestQueue requestQueue= Volley.newRequestQueue(AdminQuestionActivity.this);
       String url= Api.mainUrl+"question/"+quizId;

       StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   progressDialog.dismiss();
                   JSONObject jsonObject=new JSONObject(response);
                   Toast.makeText(AdminQuestionActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
               Toast.makeText(AdminQuestionActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
           }
       }){

           @Override
           protected Map<String, String> getParams()  {
               Map<String, String>  parms=new HashMap<String, String>();
               parms.put("question",question);
               parms.put("option1",option1);
               parms.put("option2",option2);
               parms.put("option3",option3);
               parms.put("option4",option4);
               parms.put("answerNr",selectedAnswerNumber);
               parms.put("mark",marks);
               return  parms;
           }
       };
       requestQueue.add(stringRequest);
    }



}