package com.example.diary;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button meButton = (Button) findViewById(R.id.meButton);
        final Button diaryButton = (Button) findViewById(R.id.diaryButton);
        final Button friendButton = (Button) findViewById(R.id.friendButton);
        final CardView card1 = (CardView) findViewById(R.id.card1);

        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card1.setBackgroundColor(getResources().getColor(R.color.colorGray));
                Intent meButtonintent = new Intent(MainActivity.this, MeActivity.class);
                MainActivity.this.startActivity(meButtonintent);
            }
        });
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diaryButtonintent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(diaryButtonintent);

            }
        });


    }
}
