package com.example.fquiz.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.fquiz.Admin.Adapter.TabLayoutAdapter;
import com.example.fquiz.R;
import com.google.android.material.tabs.TabLayout;

public class AdminQuizActivity extends AppCompatActivity {
    private ViewPager mviewPager;
    private TabLayout mTablayot;
    private TabLayoutAdapter sectionPagerAdapter;

    private  String categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz);

        categoryId=getIntent().getStringExtra("categoryId");

        getSupportActionBar().hide();

        mTablayot=findViewById(R.id.main_tabsid);
        mviewPager=findViewById(R.id.mainTabpagerid);

        sectionPagerAdapter=new TabLayoutAdapter(getSupportFragmentManager(),categoryId);
        mviewPager.setAdapter(sectionPagerAdapter);
        mTablayot.setupWithViewPager(mviewPager);









    }
}