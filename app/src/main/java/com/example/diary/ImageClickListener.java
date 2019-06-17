package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageClickListener implements OnClickListener {
    Context context;
    int position;
    String[] imageIDs;
    String image_date;


    public  ImageClickListener(Context context, String[] imageIDs, int position, String image_date) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.position = position;
        this.image_date = image_date;
    }
    public void onClick(View v) {
        Intent intent= new Intent(context, ImageActivity.class);
        intent.putExtra("image ID", imageIDs);
        intent.putExtra("position",position);
        intent.putExtra("image_date", image_date);
        context.startActivity(intent);
    }
}