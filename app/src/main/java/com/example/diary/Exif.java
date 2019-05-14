package com.example.diary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.media.ExifInterface.TAG_GPS_LATITUDE;

public class Exif extends AppCompatActivity {

    private static final int RQS_OPEN_IMAGE = 1;
    private static final int RQS_READ_EXTERNAL_STORAGE = 2;
    Button buttonOpen;
    TextView textUri;
    ImageView imageView;

    Uri targetUri = null;

    View.OnClickListener buttonOpenOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, RQS_OPEN_IMAGE);
                }

            };
    View.OnClickListener textUriOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (targetUri != null){
                        Bitmap bm;
                        try {
                            bm = BitmapFactory.decodeStream(
                                    getContentResolver()
                                            .openInputStream(targetUri));
                            imageView.setImageBitmap(bm);
                        } catch (FileNotFoundException e) {
                            //TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            };

    View.OnClickListener imageOnClickListener =
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if(CheckPermission_READ_EXTERNAL_STORAGE()){
                        showExif(targetUri);
                    }

                }
            };
    private boolean CheckPermission_READ_EXTERNAL_STORAGE() {
        // return true: have permission
        // return false: no permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    RQS_READ_EXTERNAL_STORAGE);

            return false;
        }else{
            return true;
        }
    }

    void showExif(Uri photoUri){

        if(photoUri != null){

            String photoPath = getRealPathFromURI(photoUri);
            try {
                ExifInterface exifInterface = new ExifInterface(photoPath);
                String exif="Exif: ";
                //exif = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                //exif +=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                //exif += exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                //exif += exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                GeoDegree geoDegree = new GeoDegree(exifInterface);
                Float latln=geoDegree.getLatitude();
               // latln+=geoDegree.getLatitudeE6();
                //String latln= exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                //latln+=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);

                //Double lati = Double.parseDouble(latln);

                //lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                //lat +=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                //long1 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                //long1+= exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                Toast.makeText(getApplicationContext(),""+latln,Toast.LENGTH_SHORT).show();

                //lati = Double.parseDouble(lat);
                //long2 = Double.parseDouble(long1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Something wrong:\n" + e.toString(),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Something wrong:\n" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getApplicationContext(),
                    "photoUri == null",
                    Toast.LENGTH_LONG).show();
        }
    };
    private String getRealPathFromURI(Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exif);

        buttonOpen = (Button) findViewById(R.id.opendocument);
        buttonOpen.setOnClickListener(buttonOpenOnClickListener);


        textUri = (TextView) findViewById(R.id.texturi);
        textUri.setOnClickListener(textUriOnClickListener);

        imageView = (ImageView)findViewById(R.id.image);
        imageView.setOnClickListener(imageOnClickListener);
    }
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Uri dataUri = data.getData();

            if (requestCode == RQS_OPEN_IMAGE) {
                targetUri = dataUri;
                textUri.setText(dataUri.toString());
                imageView.setImageBitmap(null);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RQS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showExif(targetUri);
                } else {
                    Toast.makeText(this,
                            "permission denied!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
