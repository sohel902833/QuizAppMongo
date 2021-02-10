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

import com.example.fquiz.Admin.DataModuler.PublishQuiz;
import com.example.fquiz.Admin.DataModuler.Quiz;
import com.example.fquiz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuizPublishedAdapter extends RecyclerView.Adapter<QuizPublishedAdapter.MyViewHolder> {

    Context context;
    private List<PublishQuiz> quizList=new ArrayList<>();
    private  OnItemClickListner listner;

    public QuizPublishedAdapter(Context context, List<PublishQuiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.quiz_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PublishQuiz currentItem=quizList.get(position);
        holder.quizNameTextview.setText(currentItem.getQuizName());
        holder.totalQuestionTextview.setText("Total Question: "+currentItem.getTotalQuestion()+"\nPublished for :"+currentItem.getClassName());

        if(currentItem.getImage().equals("none")){
            Picasso.get().load(R.drawable.jp1).into(holder.imageView);
        }else{
            Picasso.get().load(currentItem.getImage()).placeholder(R.drawable.jp1).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        TextView quizNameTextview,totalQuestionTextview;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quizNameTextview=itemView.findViewById(R.id.quiz_list_quizNameTextviewid);
            totalQuestionTextview=itemView.findViewById(R.id.quiz_list_TotalQuestionTextview);
            imageView=itemView.findViewById(R.id.quiz_list_Imageviewid);

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
            MenuItem publish=menu.add(Menu.NONE,1,1,"Publish Quiz");
            MenuItem delete=menu.add(Menu.NONE,2,2,"Delete");
            MenuItem update=menu.add(Menu.NONE,3,3,"Update");
            delete.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);
            publish.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:{
                            listner.onPublishQuiz(position);
                            return  true;
                        }
                        case 2:{
                            listner.onDelete(position);
                            return  true;
                        }
                        case 3:{
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
        void onPublishQuiz(int position);
        void onDelete(int position);
        void onUpdate(int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }






}
