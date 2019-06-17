package com.example.diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Adapter extends PagerAdapter
{
    PhotoViewAttacher mAttcher;

    private LayoutInflater inflater;
    private Context context;
    private String[] imageIDs;
    private int position_num;

    public Adapter(Context context,String[] imageIDs, int position)
    {
        this.context=context;
        this.imageIDs =imageIDs;
        this.position_num = position;
    }
    @Override
    public int getCount()
    {
        return (null!=imageIDs) ? imageIDs.length:0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }

    @Override
    public  Object instantiateItem(ViewGroup container, final int position)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_image,container,false);
        ImageView imageView =(ImageView) v.findViewById(R.id.imageView);
        mAttcher=new PhotoViewAttacher(imageView);
        EditText edittext= (EditText) v.findViewById(R.id.edittext);
        Bitmap bmp = BitmapFactory.decodeFile(imageIDs[position]);
        //bmp = Bitmap.createScaledBitmap(bmp,320,240,false);
        imageView.setImageBitmap(bmp);

        container.addView(v);
        return v;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.invalidate();
    }
}
