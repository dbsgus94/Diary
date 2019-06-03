package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageClickListener implements OnClickListener {
    Context context;
    int position;
    String[] imageIDs;

    public  ImageClickListener(Context context, String[] imageIDs, int position) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.position = position;
    }
    public void onClick(View v) {
        Intent intent= new Intent(context, ImageActivity.class);
        intent.putExtra("image ID", imageIDs);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }
}