package com.example.diary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SelectDateActivity extends AppCompatActivity{

    private static String pick_date_dpt = null;
    private static String pick_date_arr = null;

    Button mBtn_dpt;
    Button mBtn_arr;
    Button mBtn_save;
    TextView dpt_text,arr_text;
    Calendar c;
    DatePickerDialog dpd_dpt,dpd_arr;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_select);

        context = SelectDateActivity.this;
        dpt_text = (TextView) findViewById(R.id.dpt_date);
        arr_text = (TextView) findViewById(R.id.arr_date);
        mBtn_dpt = (Button) findViewById(R.id.btnPick_dpt);
        mBtn_arr = (Button) findViewById(R.id.btnPick_arr);
        mBtn_save =(Button) findViewById(R.id.dateSave);
        mBtn_dpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd_dpt = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        if (mMonth + 1 < 10)
                            if (mDay >= 10)
                                pick_date_dpt = (mYear + "-0" + (mMonth + 1) + "-" + mDay);
                            else
                                pick_date_dpt= (mYear + "-0" + (mMonth + 1) + "-0" + mDay);
                        else if (mDay < 10)
                            pick_date_dpt = (mYear + "-" + (mMonth + 1) + "-0" + mDay);
                        else
                            pick_date_dpt = (mYear + "-" + (mMonth + 1) + "-" + mDay);
                            dpt_text.setText(pick_date_dpt);
                    }
                }, year, month, day);
                dpd_dpt.show();
                mBtn_arr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);

                        dpd_arr = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                                if (mMonth + 1 < 10)
                                    if (mDay >= 10)
                                        pick_date_arr = (mYear + "-0" + (mMonth + 1) + "-" + mDay);
                                    else
                                        pick_date_arr = (mYear + "-0" + (mMonth + 1) + "-0" + mDay);
                                else if (mDay < 10)
                                    pick_date_arr = (mYear + "-" + (mMonth + 1) + "-0" + mDay);
                                else
                                    pick_date_arr = (mYear + "-" + (mMonth + 1) + "-" + mDay);
                                    arr_text.setText(pick_date_arr);
                            }
                        }, year, month, day);
                        dpd_arr.show();
                    }
                });


                mBtn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent diaryButtonintent = new Intent(context, MapsActivity.class);
                        diaryButtonintent.putExtra("pick_date_dpt",pick_date_dpt);
                        diaryButtonintent.putExtra("pick_date_arr",pick_date_arr);
                        context.startActivity(diaryButtonintent);
                    }
                });

            }
        });
    }
}
