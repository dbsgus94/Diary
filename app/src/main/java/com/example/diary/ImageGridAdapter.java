package com.example.diary;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageGridAdapter extends BaseAdapter {

    Context context;

    String [] imageIDs;
    String image_date;

    public ImageGridAdapter(Context context, String[] imageIDs,String image_date) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.image_date = image_date;

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
            Bitmap bmp = BitmapFactory.decodeFile(imageIDs[position]);
            //bmp = Bitmap.createScaledBitmap(bmp,320,240,false);
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);
            ImageClickListener imageViewClickListener = new ImageClickListener(context, imageIDs, position, image_date);
            imageView.setOnClickListener(imageViewClickListener);
        }
        return imageView;
    }
}
