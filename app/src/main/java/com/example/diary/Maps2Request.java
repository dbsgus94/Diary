package com.example.diary;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Maps2Request extends StringRequest {
    final static private String URL = "https://rladbsgus94.cafe24.com/ShowImage.php";
    private Map<String, String> parameters;

    public Maps2Request(String imageDate, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("imageDate",imageDate);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

