package com.example.diary;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ShowListRequest extends StringRequest {
    final static private String URL = "https://rladbsgus94.cafe24.com/ShowList.php";
    private Map<String, String> parameters;

    public ShowListRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

