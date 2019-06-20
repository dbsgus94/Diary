package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageClickListener implements OnClickListener {
    Context context;
    int position;
    String[] imageIDs;
    String[] imageLat;
    String[] imageLng;
    String image_date;
    String marker_title;
    String[] imaComment;


    public  ImageClickListener(Context context, String[] imageIDs, int position, String image_date, String[] imageLat, String[] imageLng, String marker_title, String[] imaComment) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.position = position;
        this.image_date = image_date;
        this.imageLat = imageLat;
        this.imageLng = imageLng;
        this.marker_title = marker_title;
        this.imaComment = imaComment;
    }
    public void onClick(View v) {
        Intent intent= new Intent(context, ImageActivity.class);
        intent.putExtra("image ID", imageIDs);
        intent.putExtra("imageLat", imageLat);
        intent.putExtra("imageLng", imageLng);
        intent.putExtra("position",position);
        intent.putExtra("image_date", image_date);
        intent.putExtra("marker_title", marker_title);
        intent.putExtra("imaComment",imaComment);
        context.startActivity(intent);
    }
}