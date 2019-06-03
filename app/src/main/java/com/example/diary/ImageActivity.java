package com.example.diary;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity{


    ViewPager viewPager;
    Adapter adapter;
    String[] imageIDs;
    int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        imageIDs = getIntent().getExtras().getStringArray("image ID");
        position = getIntent().getExtras().getInt("position");
        viewPager=(ViewPager)findViewById(R.id.vvv);
        adapter=new Adapter(this, imageIDs,position);
        viewPager.setAdapter(adapter);


    }


}