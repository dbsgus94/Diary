package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageClickListener implements OnClickListener {
    Context context;

    String[] imageIDs;

    public  ImageClickListener(Context context, String[] imageIDs) {
        this.context = context;
        this.imageIDs = imageIDs;
    }
    public void onClick(View v) {
        Intent intent= new Intent(context, ImageActivity.class);
        intent.putExtra("image ID", imageIDs);
        context.startActivity(intent);
    }
}