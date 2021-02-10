package com.example.fquiz.User.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fquiz.Admin.DataModuler.Question;
import com.example.fquiz.R;

import java.util.ArrayList;
import java.util.List;

public class QuizHandelerAdapter extends RecyclerView.Adapter<QuizHandelerAdapter.MyViewHolder>  {

    private RadioButton radioButton;
    private Context context;
    private Activity activity;
    private List<Question> quizList=new ArrayList<>();
    private  OnItemClickListner listner;
    int tposition=0;

    public QuizHandelerAdapter(Activity activity, Context context, List<Question> quizList) {
        this.activity=activity;
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item_list, parent, false);
     return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final Question currentItem=quizList.get(position);

            holder.questionTextview.setText(currentItem.getQuestion());
            holder.radioButton1.setText(currentItem.getOption1());
            holder.radioButton2.setText(currentItem.getOption2());
            holder.radioButton3.setText(currentItem.getOption3());
            holder.radioButton4.setText(currentItem.getOption4());
            holder.serialTextview.setText("Question "+(position+1));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null){
                        if(position!= RecyclerView.NO_POSITION){
                            listner.onClick(position,holder.quizRadioGroup,Integer.parseInt(currentItem.getAnswerNr()));
                        }
                    }
                }
            });



            holder.quizRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                    if(holder.radioButton1.isChecked()){
                            tposition=1;
                    }   else  if(holder.radioButton2.isChecked()){
                            tposition=2;
                    }  else  if(holder.radioButton3.isChecked()){
                            tposition=3;
                    } else  if(holder.radioButton4.isChecked()){
                            tposition=4;
                    }

                 if(listner!=null){
                        if(position!= RecyclerView.NO_POSITION){
                            listner.OnChooseQuestion(position,tposition,Integer.parseInt(currentItem.getAnswerNr()));
                        }
                    }


                }
            });






    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{

        private TextView questionTextview,serialTextview;

        private RadioGroup quizRadioGroup;
        private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextview=itemView.findViewById(R.id.questionTextviewid);
            quizRadioGroup=itemView.findViewById(R.id.quiz_radio_group);
            radioButton1=itemView.findViewById(R.id.quiz_radio_button1);
            radioButton2=itemView.findViewById(R.id.quiz_radio_button2);
            radioButton3=itemView.findViewById(R.id.quiz_radio_button3);
            radioButton4=itemView.findViewById(R.id.quiz_radio_button4);
            serialTextview=itemView.findViewById(R.id.questionSerialNumberTextviewid);



        }
    }

    public interface  OnItemClickListner{
        void onClick(int quizPosition, RadioGroup radioGroup, int ansNumber);
        void OnChooseQuestion(int quizPosition, int selectedAnswerNumber,int questionAnswerNumber);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }







}
