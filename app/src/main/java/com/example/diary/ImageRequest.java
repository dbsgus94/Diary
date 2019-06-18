package com.example.diary;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ImageRequest extends StringRequest {
    final static private String URL = "https://rladbsgus94.cafe24.com/ImageRegister.php";
    private Map<String, String> parameters;

    public ImageRequest(String imageComment, String imageDate, String imageTime, String markerNum, String imageLat, String imageLng, String imageSource, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("imageComment",imageComment);
        parameters.put("imageDate",imageDate);
        parameters.put("imageTime",imageTime);
        parameters.put("markerNum",markerNum);
        parameters.put("imageLat",imageLat);
        parameters.put("imageLng",imageLng);
        parameters.put("imageSource",imageSource);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
