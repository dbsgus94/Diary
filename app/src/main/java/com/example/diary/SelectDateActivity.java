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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectDateActivity extends AppCompatActivity{

    private static String pick_date_dpt = "윤현";
    private static String pick_date_arr = "현윤";

    Button mBtn_dpt;
    Button mBtn_arr;
    Button mBtn_save;
    TextView dpt_text,arr_text;
    Calendar c;
    DatePickerDialog dpd_dpt,dpd_arr;
    Context context;
    MapsActivity maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_select);

        pick_date_dpt = "윤현";
        pick_date_arr = "현윤";
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

            }
        });

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
                long begin =0;
                long end =0;
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
                    Date beginDate = (Date) formatter.parse(pick_date_dpt);
                    Date endDate = (Date) formatter.parse(pick_date_arr);
                    begin = beginDate.getTime();
                    end = endDate.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(pick_date_dpt.equals("윤현") && pick_date_arr.equals("현윤"))
                {
                    Toast.makeText(SelectDateActivity.this, "출발 날짜와 도착 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(pick_date_arr.equals("현윤"))
                {
                    Toast.makeText(SelectDateActivity.this, "도착 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(pick_date_dpt.equals("윤현"))
                {
                    Toast.makeText(SelectDateActivity.this, "출발 날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (end >= begin) {
                        Intent diaryButtonintent = new Intent(context, MapsActivity.class);
                        diaryButtonintent.putExtra("pick_date_dpt", pick_date_dpt);
                        diaryButtonintent.putExtra("pick_date_arr", pick_date_arr);
                        context.startActivity(diaryButtonintent);
                    } else if (end < begin) {
                        Toast.makeText(SelectDateActivity.this, "도착 날짜는 출발 날짜보다 이전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
