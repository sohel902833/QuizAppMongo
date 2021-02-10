package com.example.fquiz.Admin.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fquiz.Admin.Fragments.AllQuizFragment;
import com.example.fquiz.Admin.Fragments.PublishQuizFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {
    String categoryId;
    public TabLayoutAdapter(@NonNull FragmentManager fm,String categoryId) {
        super(fm);
        this.categoryId=categoryId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                AllQuizFragment allQuizFragment=new AllQuizFragment(categoryId);
                return allQuizFragment;
            }
            case 1:{
                PublishQuizFragment publishQuizFragment=new PublishQuizFragment();
                return  publishQuizFragment;
            }
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return  "All Quiz";
            case 1:
                return  "Published Quiz";
            default:
                return null;

        }
    }
}
