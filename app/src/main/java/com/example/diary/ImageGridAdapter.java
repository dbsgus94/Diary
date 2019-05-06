package com.example.diary;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter {

    Context context;

    int [] imageIDs;

    public ImageGridAdapter(Context context, int[] imageIDs) {
        this.context = context;
        this.imageIDs = imageIDs;
    }
    public int getCount() {
        return (null!=imageIDs) ? imageIDs.length:0;

    }

    public Object getItem(int position) {
        return (null != imageIDs) ? imageIDs[position]:0;
    }

    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;

        if(null != convertView)
            imageView = (ImageView)convertView;
        else {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),imageIDs[position]);
            bmp = Bitmap.createScaledBitmap(bmp,320,240,false);
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);
            ImageClickListener imageViewClickListener = new ImageClickListener(context, imageIDs[position]);
            imageView.setOnClickListener(imageViewClickListener);

        }
        return imageView;
    }
}
