package com.example.diary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MeActivity extends AppCompatActivity {
    ImageView imageView;
    //String getString = getIntent().getStringExtra("id_value");
    private static final int GET_FROM_GALLERY = 1;
    //private static String userEmail = "이메일을 입력하세요";
    //private static String phonenumber = "전화번호을 입력하세요";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        Intent intent=getIntent();
        String userID = intent.getExtras().getString("id_value");
        imageView = (ImageView) findViewById(R.id.imageToUpload);
        LinearLayout linearButton = (LinearLayout) findViewById(R.id.linearButton);
        TextView textViewID = (TextView) findViewById(R.id.textViewID);
        if (userID != null)
            textViewID.setText(userID);


        Response.Listener<String> responseLister = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonResponse = new JSONObject(response);
                    String userEmail = jsonResponse.getString("userEmail");

                    EditText textEmail = (EditText)findViewById(R.id.textEmail);
                    if (userEmail != null)
                        textEmail.setHint(userEmail);



                    String phoneNumber = jsonResponse.getString("phonenumber");

                    EditText textPhone = (EditText)findViewById(R.id.textPhone);
                    if (phoneNumber != null)
                        textPhone.setHint(phoneNumber);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        MeRequest meRequest = new MeRequest(userID,responseLister);
        RequestQueue queue = Volley.newRequestQueue(MeActivity.this);
        queue.add(meRequest);


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
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data !=null) {
            Uri selectedImage = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageURI(selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
