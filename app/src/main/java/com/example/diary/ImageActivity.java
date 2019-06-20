package com.example.diary;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity{


    ViewPager viewPager;
    Adapter adapter;
    String[] imageIDs=null;
    String[] imageLat=null;
    String[] imageLng=null;
    String image_date=null;
    String marker_title=null;
    String[] imaComment = null;
    int position=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //TextView imagedate = (TextView) findViewById(R.id.imageDate);

        imageIDs = getIntent().getExtras().getStringArray("image ID");
        imageLat = getIntent().getExtras().getStringArray("imageLat");
        imageLng = getIntent().getExtras().getStringArray("imageLng");
        position = getIntent().getExtras().getInt("position");
        marker_title = getIntent().getExtras().getString("marker_title");
        image_date = getIntent().getExtras().getString("image_date");
        imaComment = getIntent().getExtras().getStringArray("imaComment");




        viewPager=(ViewPager)findViewById(R.id.vvv);
        //imagedate.setText(image_date);

        adapter=new Adapter(this, imageIDs, position, imageLat, imageLng, image_date, marker_title,imaComment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);





    }


}