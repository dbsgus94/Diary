package com.example.diary;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ImageGridActivity extends AppCompatActivity {
    private static ArrayList<String> image_info = new ArrayList<String>();
    private static String[] imageIDs = null;
    private static String imageDate = null;
    private static String[] imageLat = null;
    private static String[] imageLng = null;
    private static String[] imaComment = null;
    private static String marker_title = null;

    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    Button btnRecord,btnStopRecord,btnPlay,btnStop;
    Boolean isRecord = false ;
    Boolean isRecordStop = false;



    final int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        image_info = getIntent().getStringArrayListExtra("image_info");
        marker_title = getIntent().getExtras().getString("marker_title");
        int al_size = image_info.size();
        imageIDs = new String[al_size];
        imageLat = new String[al_size];
        imageLng = new String[al_size];
        imaComment = new String[al_size];

        for (int i = 0; i <= al_size - 1; i++) {
            String info_img = image_info.get(i);
            String temp[] = info_img.split(":");
            imageIDs[i] = temp[0];
            imageLat[i] = temp[1];
            imageLng[i] = temp[2];
            imageDate = temp[3];
            imaComment[i] = temp[4];
        }


        setContentView(R.layout.activity_image_grid);
        GridView gridViewImages = (GridView) findViewById(R.id.gridViewImages);
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this, imageIDs,imageLat,imageLng, imageDate, marker_title, imaComment);
        gridViewImages.setAdapter(imageGridAdapter);
        btnPlay = (Button) findViewById(R.id.btnplay);
        btnRecord = (Button) findViewById(R.id.btnStartRecord);
        btnStop = (Button) findViewById(R.id.btnSTOP);
        btnStopRecord = (Button) findViewById(R.id.btnStopRecord);


        TextView griddate = (TextView) findViewById(R.id.gridDate);
        //griddate.setText(imageDate);

        if (!checkPermissionFromDevice()) {
            requestPermission();
        }

        //Toast.makeText(this, image_info.toString(), Toast.LENGTH_SHORT).show();
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecord = true;
                //Toast.makeText(ImageGridActivity.this, image_info.get(0), Toast.LENGTH_LONG).show();
                if (checkPermissionFromDevice()) {
                    pathSave = Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/"
                            + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    Toast.makeText(ImageGridActivity.this, "Recording..." , Toast.LENGTH_SHORT).show();
                    try {
                        mediaRecorder.prepare();
                        if (isRecord)
                            mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);
                } else {
                    requestPermission();
                }
                isRecord = false;
            }

        });
        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecord = false;
                mediaRecorder.stop();
                if(isRecordStop)
                    btnStopRecord.setEnabled(false);
                btnPlay.setEnabled(true);
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStop.setEnabled(true);
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(false);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(pathSave);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(ImageGridActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }
            }
        });
    }
    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;


    }
}