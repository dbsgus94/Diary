package com.example.diary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.net.URLEncoder;


public class MeActivity extends AppCompatActivity {

    private AlertDialog dialog;
    ImageView imageView;
    //String getString = getIntent().getStringExtra("id_value");
    private static final int GET_FROM_GALLERY = 1;
    private static String bitImage = null;
    private static Bitmap bitmap = null;
    private static String phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        EditText textEmail = (EditText) findViewById(R.id.textEmail);
        EditText textPhone = (EditText) findViewById(R.id.textPhone);


        Intent intent = getIntent();
        String userID = intent.getExtras().getString("id_value");
        imageView = (ImageView) findViewById(R.id.imageToUpload);
        LinearLayout linearButton = (LinearLayout) findViewById(R.id.linearButton);
        TextView textViewID = (TextView) findViewById(R.id.textID);
        if (userID != null)
            textViewID.setText(userID);


        Response.Listener<String> responseLister = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    String userEmail = jsonResponse.getString("userEmail");
                    if (userEmail != null)
                        textEmail.setText(userEmail);
                    else
                        textEmail.setText("이메일을 입력하세요.");

                    phoneNumber = jsonResponse.getString("phoneNumber");
                    if (phoneNumber != null)
                        textPhone.setText(phoneNumber);
                    else
                        textPhone.setText("전화번호를 입력하세요.");

                    String db_picture = jsonResponse.getString("picture");
                    bitmap = getImage(db_picture);
                    ImageView pic = (ImageView)findViewById(R.id.imageToUpload);
                    if (bitmap != null)
                        pic.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        MeRequest meRequest = new MeRequest(userID, responseLister);
        RequestQueue queue = Volley.newRequestQueue(MeActivity.this);
        queue.add(meRequest);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = textEmail.getText().toString();
                String phonenumber = textPhone.getText().toString();
                String userID = intent.getExtras().getString("id_value");
                String picture = bitImage;
                if (userEmail.equals("") || phonenumber.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MeActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MeActivity.this);
                                dialog = builder.setMessage("회원 정보가 수정되었습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MeActivity.this);
                                dialog = builder.setMessage("회원 정보 수정에 실패했습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                SaveRequest saveRequest = new SaveRequest(userID, userEmail, phonenumber, picture, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MeActivity.this);
                queue.add(saveRequest);
            }
        });




        linearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();


            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
                bitImage = getStringImage(bitmap);
                Toast.makeText(this, ""+selectedImage, Toast.LENGTH_LONG).show();


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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