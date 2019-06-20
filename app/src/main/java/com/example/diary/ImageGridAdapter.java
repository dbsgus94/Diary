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
import android.graphics.Matrix;
import android.support.media.ExifInterface;

public class ImageGridAdapter extends BaseAdapter {

    Context context;
    String [] imageIDs;
    String [] imageLat;
    String [] imageLng;
    String [] imaComment;
    String image_date;
    String marker_title;

    public ImageGridAdapter(Context context, String[] imageIDs,String[] imageLat ,String[] imageLng, String image_date, String marker_title, String[] imaComment) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.imageLat = imageLat;
        this.imageLng = imageLng;
        this.image_date = image_date;
        this.marker_title = marker_title;
        this.imaComment = imaComment;

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
            ImageClickListener imageViewClickListener = new ImageClickListener(context, imageIDs, position, image_date, imageLat, imageLng, marker_title,imaComment);
            imageView.setOnClickListener(imageViewClickListener);
        }
        return imageView;
    }
}