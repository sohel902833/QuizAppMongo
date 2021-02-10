package com.example.fquiz.Admin.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fquiz.Admin.DataModuler.Category;
import com.example.fquiz.Admin.DataModuler.Question;
import com.example.fquiz.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder> {

    Context context;
    private List<Question> questionList=new ArrayList<>();
    private  OnItemClickListner listner;

    public QuestionListAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.question_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Question currentItem=questionList.get(position);


        holder.questionText.setText("Q "+(position+1)+":"+currentItem.getQuestion());
        holder.option1Text.setText("Option 1: "+currentItem.getOption1());
        holder.option2Text.setText("Option 2: "+currentItem.getOption2());
        holder.option3Text.setText("Option 3: "+currentItem.getOption3());
        holder.option4Text.setText("Option 4: "+currentItem.getOption4());
        holder.answerNumberText.setText("Answer Number: "+currentItem.getAnswerNr()+" ...  Mark: "+currentItem.getMark());


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        TextView questionText,option1Text,option2Text,option3Text,option4Text,answerNumberText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            questionText=itemView.findViewById(R.id.question_list_QuestionTextviewid);
            option1Text=itemView.findViewById(R.id.question_list_Option1Textview);
            option2Text=itemView.findViewById(R.id.question_list_Option2Textview);
            option3Text=itemView.findViewById(R.id.question_list_Option3Textview);
            option4Text=itemView.findViewById(R.id.question_list_Option4Textview);
            answerNumberText=itemView.findViewById(R.id.question_list_AnswerNumberTextview);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("choose an action");
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete");
            MenuItem update=menu.add(Menu.NONE,2,2,"Update");
            delete.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:{
                            listner.onDelete(position);
                            return  true;
                        }
                        case 2:{
                            listner.onUpdate(position);
                            return  true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onDelete(int position);
        void onUpdate(int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }






}
