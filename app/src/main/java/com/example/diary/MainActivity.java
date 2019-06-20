package com.example.diary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button meButton = (Button) findViewById(R.id.meButton);
        final Button diaryButton = (Button) findViewById(R.id.diaryButton);
        final Button friendButton = (Button) findViewById(R.id.friendButton);
        final CardView card1 = (CardView) findViewById(R.id.card1);
        checkPermission();

        meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String userID = intent.getExtras().getString("id_value");

                Intent meButtonintent = new Intent(MainActivity.this, MeActivity.class);
                meButtonintent.putExtra("id_value",userID);
                MainActivity.this.startActivity(meButtonintent);

            }
        });
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                //String userID = intent.getExtras().getString("id_value");
                String userID = "ko";
                Intent listviewintent = new Intent(MainActivity.this, ListviewActivity.class);
                listviewintent.putExtra("id_value",userID);
                MainActivity.this.startActivity(listviewintent);
            }
        });


        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diaryButtonintent = new Intent(MainActivity.this, SelectDateActivity.class);
                MainActivity.this.startActivity(diaryButtonintent);

            }
        });


    }
    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수

            } else {
                //Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
