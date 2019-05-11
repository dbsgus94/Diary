package com.example.diary;

import android.content.Context;
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

    private int [] imageIDs = new int [] {
            R.drawable.gallery_image_01,
            R.drawable.gallery_image_02,
            R.drawable.gallery_image_03,
            R.drawable.gallery_image_04,
            R.drawable.gallery_image_05,
            R.drawable.gallery_image_06,
            R.drawable.gallery_image_07,
            R.drawable.gallery_image_08,
            R.drawable.gallery_image_09,
            R.drawable.gallery_image_10,
    };
    private LayoutInflater inflater;
    private Context context;


    public Adapter(Context context)
    {
        this.context=context;
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
    public  Object instantiateItem(ViewGroup container, int position)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_image,container,false);
        ImageView imageView =(ImageView) v.findViewById(R.id.imageView);
        mAttcher=new PhotoViewAttacher(imageView);
        EditText edittext= (EditText) v.findViewById(R.id.edittext);
        imageView.setImageResource(imageIDs[position]);
        container.addView(v);
        return v;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.invalidate();
    }
}
