package com.example.fquiz.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fquiz.Admin.AdminMainActivity;
import com.example.fquiz.Admin.AdminQuestionActivity;
import com.example.fquiz.Admin.DataModuler.PublishQuiz;
import com.example.fquiz.Admin.DataModuler.Question;
import com.example.fquiz.Admin.DataModuler.Quiz;
import com.example.fquiz.Api;
import com.example.fquiz.MainActivity;
import com.example.fquiz.R;
import com.example.fquiz.User.Adapter.QuizHandelerAdapter;
import com.example.fquiz.User.DataModuler.QuizAnswer;
import com.example.fquiz.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;


    RecyclerView recyclerView;
    private TextView timeCountDownText;
    private CountDownTimer countDownTimer;
    private  long timeLeftInMIllis;
    // private  long timeLeftInMIllis=30000;

    List<Question> questionList=new ArrayList<>();
    List<QuizAnswer> quizAnswerList=new ArrayList<>();

    private QuizHandelerAdapter adapter;

    private RadioButton radioButton;
    private Button submitButton;





    String id,quizId,time;

    private  int mark=0;
    private  int correctCount=0;
    private  int falseCount=0;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        progressDialog=new ProgressDialog(this);
        quizId=getIntent().getStringExtra("quizId");
        id=getIntent().getStringExtra("id");
        time=getIntent().getStringExtra("time");
        timeLeftInMIllis=Long.parseLong(time);
         users=new Users(QuizActivity.this);



        recyclerView=findViewById(R.id.quizListRecyclerviewid);
        timeCountDownText=findViewById(R.id.countDownTimerTextviewid);
        submitButton=findViewById(R.id.quizPublishButtonid);
        startCountDown();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new QuizHandelerAdapter(QuizActivity.this,this,questionList);
        recyclerView.setAdapter(adapter);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(questionList.size()==quizAnswerList.size() && timeLeftInMIllis!=0){
                    submitQuizAnswer();
                }else if(timeLeftInMIllis==0){
                    Toast.makeText(QuizActivity.this, "Time End.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuizActivity.this, "Please Choose All The Questions", Toast.LENGTH_SHORT).show();
                }
            }
        });




        adapter.setOnItemClickListner(new QuizHandelerAdapter.OnItemClickListner() {
            @Override
            public void onClick(int quizPosition, RadioGroup radioGroup, int ansNumber) {
                Toast.makeText(QuizActivity.this, "Quiz position: "+quizPosition, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void OnChooseQuestion(int quizPosition, int selectedAnswerNumber, int questionAnswerNumber) {
                Question currentItem = questionList.get(quizPosition);

                int count=0;
                if(quizAnswerList.size()>0){
                    for(int i=0; i<quizAnswerList.size(); i++){

                        if(quizAnswerList.get(i).getPosition()==quizPosition){
                            count++;
                            QuizAnswer quizAnswer=new QuizAnswer(quizPosition,currentItem.getQuestion(),currentItem.getOption1(),currentItem.getOption2(),currentItem.getOption3(),currentItem.getOption4(),Integer.parseInt(currentItem.getAnswerNr()),selectedAnswerNumber);
                            quizAnswerList.set(i,quizAnswer);
                        }
                    }

                    if(count==0){
                        QuizAnswer quizAnswer=new QuizAnswer(quizPosition,currentItem.getQuestion(),currentItem.getOption1(),currentItem.getOption2(),currentItem.getOption3(),currentItem.getOption4(),Integer.parseInt(currentItem.getAnswerNr()),selectedAnswerNumber);
                        quizAnswerList.add(quizAnswer);
                    }
                }

                else{
                    QuizAnswer quizAnswer=new QuizAnswer(quizPosition,currentItem.getQuestion(),currentItem.getOption1(),currentItem.getOption2(),currentItem.getOption3(),currentItem.getOption4(),Integer.parseInt(currentItem.getAnswerNr()),selectedAnswerNumber);
                    quizAnswerList.add(quizAnswer);
                }
            }
        });



    }
    private  void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftInMIllis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMIllis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                submitButton.setText("Time End");
                submitButton.setEnabled(false);
                timeLeftInMIllis=0;
                updateCountDownText();
            }
        }.start();
    }
    private  void updateCountDownText(){

        int minutes=(int)(timeLeftInMIllis/1000/60);
        int seconds=(int)(timeLeftInMIllis/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        timeCountDownText.setText("Time Left: "+timeFormatted);
        if(timeLeftInMIllis<30000){
            timeCountDownText.setTextColor(Color.RED);
        }else{
            timeCountDownText.setTextColor(timeCountDownText.getTextColors());
        }
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
        RequestQueue requestQueue= Volley.newRequestQueue(QuizActivity.this);
        String url= Api.mainUrl+"question/"+quizId;

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
                        adapter.notifyDataSetChanged();
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
                Toast.makeText(QuizActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        addStudentDiolouge();

    }
    public void addStudentDiolouge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(QuizActivity.this);
        View view=getLayoutInflater().inflate(R.layout.exit_diolouge,null);
        builder.setView(view);

        Button positiveButton=view.findViewById(R.id.exit_positivebutton);
        Button negativeButtonId=view.findViewById(R.id.exit_negativebutton);

        final AlertDialog dialog=builder.create();
        dialog.show();


        negativeButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void submitQuizAnswer() {
        markCalculator();
        progressDialog.setMessage("Posting Your Result");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(QuizActivity.this);
        String url= Api.mainUrl+"answer/"+id;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    Intent intent=new Intent(QuizActivity.this,QuizResultActivity.class);
                    intent.putExtra("answer", (Serializable) quizAnswerList);
                    intent.putExtra("totalQuestion",questionList.size());
                    startActivity(intent);
                    saveData();
                    Toast.makeText(QuizActivity.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(QuizActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams()  {

               Long totalTime=Long.parseLong(time);
                Long timeSpent=totalTime-timeLeftInMIllis;

                Map<String, String>  parms=new HashMap<String, String>();
                parms.put("userName",users.getUserName());
                parms.put("userId",users.getUserId());
                parms.put("mark", String.valueOf(mark));
                parms.put("timeSpent", String.valueOf(timeSpent));
                return  parms;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void saveData() {

        SharedPreferences sharedPreferences=getSharedPreferences("quizData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String data=users.getUserId()+quizId;
        editor.putString(data,"done");
        editor.commit();
    }

    private void markCalculator() {
        if(quizAnswerList.size()>0) {
            for (int i = 0; i <quizAnswerList.size(); i++) {
                QuizAnswer currentItem = quizAnswerList.get(i);
                if (currentItem.getAnswerNumber() == currentItem.getProvidedAnswerNumber()) {
                    mark++;
                    correctCount++;
                } else {
                    falseCount++;
                }


            }
        }
    }
}