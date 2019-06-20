package com.example.diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class ListviewActivity extends AppCompatActivity {

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private static String depart=null;
    private static String arrive=null;
    private static String location_name=null;
    private String userID = null;
    private static ArrayList<String> listdata = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ListView listView=(ListView)findViewById(R.id.listv);
        String[] items={};
        arrayList = new ArrayList<>(Arrays.asList(items));
        adapter=new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        Button btAdd = (Button)findViewById(R.id.btadd);
        Button btver = (Button)findViewById(R.id.btver);

        listdata.clear();
        Intent intent=getIntent();
        userID = intent.getExtras().getString("id_value");

        Response.Listener<String> responseLister = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(ListviewActivity.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();
                    if (jsonArray != null) {
                        for (int i=0;i<jsonArray.length();i++){
                            listdata.add(jsonArray.getString(i));
                        }
                    }

                    for(int i=0;i<listdata.size();i++)
                    {
                        String [] temp;
                        String [] val;
                        String fin;
                        temp = listdata.get(i).split(",");
                        val = temp[1].split(":");

                        val[1] = val[1].replaceAll("\"","");
                        val[3] = val[3].replaceAll("\"","");

                        fin = val[1] + ":::" + val[2] + "~" + val[3]+":::" + userID;
                        arrayList.add(fin);
                        adapter.notifyDataSetChanged();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ShowListRequest showlistRequest = new ShowListRequest(userID, responseLister);
        RequestQueue queue = Volley.newRequestQueue(ListviewActivity.this);
        queue.add(showlistRequest);
        //arrayList.add(location_name + "-" + depart + " ~ " + arrive);

        btver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                //Toast.makeText(ListviewActivity.this, arrayList.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selDateintent = new Intent(ListviewActivity.this, SelectDateActivity.class);
                selDateintent.putExtra("id_value",userID);
                ListviewActivity.this.startActivity(selDateintent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ListviewActivity.this, MapsActivity2.class);

                String temp = arrayList.get(position);
                String[] val = temp.split(":::");
                String[] val2 = val[1].split("~");


                myIntent.putExtra("depart",val2[0]);
                myIntent.putExtra("arrive",val2[1]);
                myIntent.putExtra("id_value",userID);
                ListviewActivity.this.startActivity(myIntent);



            }
        });
    }
}