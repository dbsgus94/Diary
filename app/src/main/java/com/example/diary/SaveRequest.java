package com.example.diary;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SaveRequest extends StringRequest {
    final static private String URL = "https://rladbsgus94.cafe24.com/SaveUserinfo.php";
    private Map<String, String> parameters;

    public SaveRequest(String userID, String userEmail, String phonenumber,String picture, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userEmail",userEmail);
        parameters.put("phonenumber",phonenumber);
        parameters.put("picture",picture);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
