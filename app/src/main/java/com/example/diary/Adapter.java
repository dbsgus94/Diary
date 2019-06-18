package com.example.diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.support.media.ExifInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Adapter extends PagerAdapter
{
    PhotoViewAttacher mAttcher;

    private LayoutInflater inflater;
    private Context context;
    private String[] imageIDs;
    private String[] imageLat_here;
    private String[] imageLng_here;
    private String image_date;
    private int position_num;
    private AlertDialog dialog;
    private String imageSource;
    private String marker_title;


    public Adapter(Context context, String[] imageIDs, int position, String[] imageLat, String[] imageLng, String image_date, String marker_title)
    {
        this.context=context;
        this.imageIDs =imageIDs;
        this.position_num = position;
        this.imageLat_here = imageLat;
        this.imageLng_here = imageLng;
        this.image_date = image_date;
        this.marker_title = marker_title;

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
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeFile(imageIDs[position],options);
        imageSource = getStringImage(bmp);

        String string_poistion = Integer.toString(position);

        String imageDate = image_date;
        String imageTime = string_poistion;
        String markerNum = marker_title;
        String imageLat = imageLat_here[position];
        String imageLng = imageLng_here[position];





        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imageComment = edittext.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                dialog = builder.setMessage("일기를 저장했습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                dialog = builder.setMessage("일기 저장에 실패했습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Toast.makeText(context, marker_title, Toast.LENGTH_SHORT).show();


                ImageRequest imageRequest = new ImageRequest(imageComment, imageDate, imageTime, markerNum, imageLat, imageLng, imageSource, responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(imageRequest);
            }
        });
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


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public Bitmap getImage(String s) {
        byte[] bytes_image = Base64.decode(s, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes_image, 0, bytes_image.length);
        return bitmap;
    }
}
