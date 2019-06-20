package com.example.diary;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ListviewRequest extends StringRequest {
    final static private String URL = "https://rladbsgus94.cafe24.com/DiaryList.php";
    private Map<String, String> parameters;

    public ListviewRequest(String diaryTitle, String diaryDepart, String diaryArrive, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();

        String temp = diaryTitle + ":" + diaryDepart + ":"+ diaryArrive;
        parameters.put("diaryTitle",temp);
        parameters.put("userID",userID);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
