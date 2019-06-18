package com.example.diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
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
        Button postBtn = (Button) v.findViewById(R.id.postBtn);
        EditText edittext= (EditText) v.findViewById(R.id.edittext);
        Bitmap bmp = BitmapFactory.decodeFile(imageIDs[position]);
        //bmp = Bitmap.createScaledBitmap(bmp,320,240,false);

        try {
            // 이미지를 상황에 맞게 회전시킨다
            ExifInterface exif = new ExifInterface(imageIDs[position]);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);

            Matrix mat = new Matrix();
            mat.postRotate(exifDegree);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);


        } catch (Exception e) {

        }

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