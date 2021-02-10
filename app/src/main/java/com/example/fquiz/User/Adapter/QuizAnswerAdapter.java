package com.example.fquiz.User.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fquiz.R;
import com.example.fquiz.User.DataModuler.QuizAnswer;

import java.util.ArrayList;
import java.util.List;

public class QuizAnswerAdapter extends RecyclerView.Adapter<QuizAnswerAdapter.MyViewHolder>  {

    private RadioButton radioButton;
    private Context context;
    private List<QuizAnswer> quizList=new ArrayList<>();

    public QuizAnswerAdapter(Context context, List<QuizAnswer> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_answer_item_list, parent, false);
     return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final QuizAnswer currentItem=quizList.get(position);

            holder.questionTextview.setText(currentItem.getQuestion());
            holder.radioButton1.setText(currentItem.getOption1());
            holder.radioButton2.setText(currentItem.getOption2());
            holder.radioButton3.setText(currentItem.getOption3());
            holder.radioButton4.setText(currentItem.getOption4());
            holder.serialTextview.setText("Question "+(position+1));


            holder.noteTextview.setText("");



            if(currentItem.getAnswerNumber()==currentItem.getProvidedAnswerNumber()){

                holder.noteTextview.setText("Answer : True\n");

                switch (currentItem.getAnswerNumber()){
                    case 1:
                        holder.radioButton1.setTextColor(Color.GREEN);
                        holder.radioButton1.setChecked(true);
                        break;
                    case 2:
                        holder.radioButton2.setTextColor(Color.GREEN);
                        holder.radioButton2.setChecked(true);
                        break;
                    case 3:
                        holder.radioButton3.setChecked(true);
                        holder.radioButton3.setTextColor(Color.GREEN);
                        break;
                    case 4:
                        holder.radioButton4.setChecked(true);
                        holder.radioButton4.setTextColor(Color.GREEN);
                        break;
                }
            }else if(currentItem.getAnswerNumber()!=currentItem.getProvidedAnswerNumber()){

                switch (currentItem.getAnswerNumber()){
                    case 1:
                        holder.noteTextview.setText("Answer : False\n Correct Ans : "+currentItem.getOption1());
                        holder.radioButton1.setChecked(true);
                        holder.radioButton1.setTextColor(Color.GREEN);
                        break;
                    case 2:
                        holder.noteTextview.setText("Answer : False\n Correct Ans : "+currentItem.getOption2());
                        holder.radioButton2.setChecked(true);
                        holder.radioButton2.setTextColor(Color.GREEN);
                        break;
                    case 3:
                        holder.noteTextview.setText("Answer : False\n Correct Ans : "+currentItem.getOption3());
                        holder.radioButton3.setChecked(true);
                        holder.radioButton3.setTextColor(Color.GREEN);
                        break;
                    case 4:
                        holder.noteTextview.setText("Answer : False\n Correct Ans : "+currentItem.getOption4());
                        holder.radioButton4.setChecked(true);
                        holder.radioButton4.setTextColor(Color.GREEN);
                        break;
                }

                switch (currentItem.getProvidedAnswerNumber()){
                    case 1:
                        holder.radioButton1.setChecked(true);
                        holder.radioButton1.setTextColor(Color.RED);
                        break;
                    case 2:
                        holder.radioButton2.setChecked(true);
                        holder.radioButton2.setTextColor(Color.RED);
                        break;
                    case 3:
                        holder.radioButton3.setChecked(true);
                        holder.radioButton3.setTextColor(Color.RED);
                        break;
                    case 4:
                        holder.radioButton4.setChecked(true);
                        holder.radioButton4.setTextColor(Color.RED);
                        break;
                }
            }






    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{

        private TextView questionTextview,serialTextview,noteTextview;

       private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextview=itemView.findViewById(R.id.ansquestionTextviewid);
           radioButton1=itemView.findViewById(R.id.ansquiz_radio_button1);
            radioButton2=itemView.findViewById(R.id.ansquiz_radio_button2);
            radioButton3=itemView.findViewById(R.id.ansquiz_radio_button3);
            radioButton4=itemView.findViewById(R.id.ansquiz_radio_button4);
  serialTextview=itemView.findViewById(R.id.ansquestionSerialNumberTextviewid);
        noteTextview=itemView.findViewById(R.id.noteTextviewid);


        }
    }







}
