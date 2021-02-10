package com.example.fquiz.Admin.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fquiz.Admin.DataModuler.Quiz;
import com.example.fquiz.Admin.DataModuler.UserQuizAnswer;
import com.example.fquiz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuizAnswerAdapter extends RecyclerView.Adapter<QuizAnswerAdapter.MyViewHolder> {

    Context context;
    private List<UserQuizAnswer> quizAnswerList=new ArrayList<>();
    private  OnItemClickListner listner;

    public QuizAnswerAdapter(Context context, List<UserQuizAnswer> quizAnswerList) {
        this.context = context;
        this.quizAnswerList = quizAnswerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_quiz_answer_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserQuizAnswer currentItem=quizAnswerList.get(position);

        holder.userNameText.setText(currentItem.getUserName());
        holder.markText.setText("Marks : "+currentItem.getMark());

        Long timeSpent=Long.parseLong(currentItem.getTimeSpent());
        int minutes=(int)(timeSpent/1000/60);
        int seconds=(int)(timeSpent/1000)%60;

        String timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        holder.timeSpentText.setText("Time Spent:> "+timeFormatted);



    }

    @Override
    public int getItemCount() {
        return quizAnswerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView userNameText,markText,timeSpentText;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameText=itemView.findViewById(R.id.userAnswer_userNameTextviewid);
            markText=itemView.findViewById(R.id.userAnswer_markTextviewid);
            timeSpentText=itemView.findViewById(R.id.userAnswer_timeSpentTextviewid);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    listner.onItemClick(position);
                }
            }
        }

    }

    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onPublishQuiz(int position);
        void onDelete(int position);
        void onUpdate(int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }
}
