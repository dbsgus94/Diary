package com.example.diary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button meButton = (Button) findViewById(R.id.meButton);
        final Button diaryButton = (Button) findViewById(R.id.diaryButton);
        final Button friendButton = (Button) findViewById(R.id.friendButton);
        final Button galleryButton =(Button) findViewById(R.id.galleryButton);
        final CardView card1 = (CardView) findViewById(R.id.card1);

        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card1.setBackgroundColor(getResources().getColor(R.color.colorGray));

                Intent intent=getIntent();
                String userID = intent.getExtras().getString("id_value");;

                Intent meButtonintent = new Intent(MainActivity.this, MeActivity.class);
                meButtonintent.putExtra("id_value",userID);
                MainActivity.this.startActivity(meButtonintent);
            }
        });
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                dialog = builder.setMessage("카메라 -> 설정 -> 위치태그 허용").setPositiveButton("확인",null).create();
                dialog.show();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent diaryButtonintent = new Intent(MainActivity.this, MapsActivity.class);
                        MainActivity.this.startActivity(diaryButtonintent);
                    }
                },100);



            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryButtonIntent = new Intent(MainActivity.this, ImageGridActivity.class);
                MainActivity.this.startActivity(galleryButtonIntent);
            }
        });


    }
}
