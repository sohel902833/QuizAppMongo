package com.example.fquiz.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fquiz.MainActivity;
import com.example.fquiz.R;
import com.example.fquiz.User.Adapter.QuizAnswerAdapter;
import com.example.fquiz.User.DataModuler.QuizAnswer;

import java.util.ArrayList;
import java.util.List;

public class QuizResultActivity extends AppCompatActivity {

    private TextView quizResultTextview;
    private RecyclerView quizResultRecyclerView;
    private  int totalQuestion,currectAnswer;
    private List<QuizAnswer> quizAnswerList=new ArrayList<>();

    private QuizAnswerAdapter adapter;
    private  int mark=0;
    private  int correctCount=0;
    private  int falseCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);


        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){

            quizAnswerList = (ArrayList<QuizAnswer>) getIntent().getSerializableExtra("answer");
            totalQuestion=getIntent().getIntExtra("totalQuestion",0);
        }



        quizResultTextview=findViewById(R.id.quizResultTextviewid);
        quizResultRecyclerView=findViewById(R.id.quizResultRecyclerviewid);
        quizResultRecyclerView.setHasFixedSize(true);
        quizResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter=new QuizAnswerAdapter(this,quizAnswerList);

        markCalculator();



        quizResultTextview.setText("Total Marks: "+mark+"\nTotal Question : "+quizAnswerList.size()+"\n Answer Correct :  "+correctCount+"\n Answer Wrong : "+falseCount);


        quizResultRecyclerView.setAdapter(adapter);







    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(QuizResultActivity.this, MainActivity.class));
        finish();
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