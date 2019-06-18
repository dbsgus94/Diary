package com.example.diary;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.support.media.ExifInterface;
import android.widget.TextView;
import android.graphics.Matrix;

import static com.android.volley.VolleyLog.setTag;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static int targetHeight;
    private static final int RQS_OPEN_IMAGE = 1;
    private static final int RQS_READ_EXTERNAL_STORAGE = 2;
    private static String photoPath;
    public int i = 0;
    private AlertDialog dialog;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;
    private PolylineOptions polylineOptions;
    private List <Polyline> polyline = new ArrayList<Polyline>();
    private ArrayList<LatLng> arrayPoints;
    private Button btn_timer_start;
    public static Context context;
    private boolean isBtnStart = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private static String pick_date_dpt = null;
    private static String pick_date_arr = null;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static int flag = 0;
    private ArrayList<String> image_info = new ArrayList<String>();//image_info[0] = 사진주소 image_info[1] = lat image_info[2] = longT image_info[3] = date
    private ArrayList<String> images;
    private Spinner spinner2;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private String select_date;
    private int cnt;
    private int cnt2;
    private int cnt3;
    private ArrayList<String> marker_list = new ArrayList<>();
    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    private ArrayList<LatLng> points;

    public static final int PATTERN_DASH_LENGTH_PX = 20;
    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소
    Location mCurrentLocatiion;
    LatLng currentPosition;
    Uri targetUri = null;

    float[] showExif(String s_path) {
        float lat_long[] = {0,0};
        if (s_path != null) {
            photoPath = s_path;
            try {
                ExifInterface exifInterface = new ExifInterface(photoPath);
                GeoDegree geoDegree = new GeoDegree(exifInterface);
                lat_long[0] = geoDegree.getLatitude();
                lat_long[1] = geoDegree.getLongitude();
                //Toast.makeText(getApplicationContext(), "" + lat + "\n" + longT, Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Something wrong:\n" + e.toString(),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Something wrong:\n" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }catch (NullPointerException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),
                //        "Something wrong:\n" + e.toString(),
                //        Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(),
                    "photoUri == null",
                    Toast.LENGTH_LONG).show();
        }
        return lat_long;
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private String getRealPathFromURI(Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    private void init() {

        arrayPoints = new ArrayList<LatLng>();
    }


    private ArrayList<String> getPathOfAllImages(String t_date) {
        ArrayList<String> result = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        int lastIndex;
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;
            if (!TextUtils.isEmpty(absolutePathOfImage)) {
                File file = new File(absolutePathOfImage);
                Date lastModDate = new Date(file.lastModified());
                String date_to_string = new SimpleDateFormat("yyyy-MM-dd").format(lastModDate);
                if (date_to_string.equals(t_date)) {
                    result.add(absolutePathOfImage);

                }
            }
        }

        for (String string : result) {
            Log.i("getPathOfAllImages", "|" + string + "|");
        }
        return result;
    }

    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MapsActivity.this;
        points = new ArrayList<LatLng>();
        points = new ArrayList<LatLng>();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_maps);

        MapsInitializer.initialize(getApplicationContext());

        mLayout = findViewById(R.id.layout_maps);

        Log.d(TAG, "onCreate");

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //추가한 부분


        pick_date_dpt = getIntent().getExtras().getString("pick_date_dpt");
        pick_date_arr = getIntent().getExtras().getString("pick_date_arr");

        int diffDays = doDiffOfDate(pick_date_dpt, pick_date_arr);
        //Toast.makeText(MapsActivity.this, ""+diffDays, Toast.LENGTH_SHORT).show();
        arrayList.add("전체 일정");
        for (int i = 0; i <= diffDays; i++) {
            String loop_date = null;
            if (i == 0) {
                loop_date = pick_date_dpt;
                arrayList.add(loop_date);
            } else if (i == diffDays) {
                loop_date = pick_date_arr;
                arrayList.add(loop_date);
            }
            else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                Date date_d = null;
                try {
                    date_d = dateFormat.parse(pick_date_dpt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date_d);
                cal.add(Calendar.DATE, i);
                loop_date = dateFormat.format(cal.getTime());
                arrayList.add(loop_date);

            }

        }

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setAdapter(arrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayList.get(i) == "전체 일정")
                {
                    select_date = "전체 일정";
                }
                else {
                    select_date = arrayList.get(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                select_date = "전체 일정";
            }
        });



        //여기까지
        this.init();
    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }
        }

    };


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mGoogleMap.setMyLocationEnabled(true);

        }

    }
    public int doDiffOfDate(String start, String end){
        int temp =0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
            Date beginDate = (Date) formatter.parse(start);
            Date endDate = (Date) formatter.parse(end);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = endDate.getTime() - beginDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            temp = (int) diffDays;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        //여기
        Log.d(TAG, "onMapReady :");
        mGoogleMap = googleMap;


        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            startLocationUpdates(); // 3. 위치 업데이트 시작


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(MapsActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }


        }

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        btn_timer_start = (Button) findViewById(R.id.btn_timer_start);
        btn_timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleMap.clear();
                image_info.clear();
                arrayPoints.clear();
                marker_list.clear();

                cnt = 0;
                cnt2 = 0;
                cnt3 = 0;

                int print_diffDays;
                if (select_date=="전체 일정"){
                    print_diffDays = doDiffOfDate(pick_date_dpt, pick_date_arr);

                    for (int i = 0; i <=print_diffDays; i++) {
                        String loop_date = null;
                        if (i == 0) {
                            loop_date = pick_date_dpt;
                        } else if (i == print_diffDays) {
                            loop_date = pick_date_arr;
                        }
                        else {

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                            Date date_d = null;

                            try {
                                date_d = dateFormat.parse(pick_date_dpt);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date_d);
                            cal.add(Calendar.DATE, i);
                            loop_date = dateFormat.format(cal.getTime());

                        }
                        float[] out_latlong;

                        images = getPathOfAllImages(loop_date);
                        for (String string : images) {
                            //String path_pho = images.get(0);s
                            //Uri uri_pho = getUriFromPath(path_pho);
                            out_latlong = showExif(string);
                            if (out_latlong[0] != 0 && out_latlong[1] != 0) {
                                cnt3 = cnt3+1;
                                image_info.add(string + ":" + Float.toString(out_latlong[0]) + ":" + Float.toString(out_latlong[1]) + ":" + loop_date);
                                //Toast.makeText(context, images.toString(), Toast.LENGTH_SHORT).show();
                                LatLng exifLatLng = new LatLng(out_latlong[0],out_latlong[1]);
                                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                                Bitmap bmp = Bitmap.createBitmap(170, 170, conf);
                                Canvas canvas1 = new Canvas(bmp);
                                canvas1.drawColor(Color.WHITE);
                                Paint color = new Paint();
                                color.setTextSize(35);
                                color.setColor(Color.BLACK);
                                Bitmap resize_bmp = resizeBitmapImg(BitmapFactory.decodeFile(photoPath));
                                try {
                                    // 이미지를 상황에 맞게 회전시킨다
                                    ExifInterface exif = new ExifInterface(photoPath);
                                    int exifOrientation = exif.getAttributeInt(
                                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                    int exifDegree = exifOrientationToDegrees(exifOrientation);

                                    Matrix mat = new Matrix();
                                    mat.postRotate(exifDegree);
                                    resize_bmp = Bitmap.createBitmap(resize_bmp, 0, 0,resize_bmp.getWidth(),resize_bmp.getHeight(), mat, true);


                                } catch (Exception e) {

                                }
                                canvas1.drawBitmap(resize_bmp, resize_bmp.getHeight()*(1/20)+10, resize_bmp.getWidth()*(1/20)+10, null);

                                String [] info1;
                                String [] info2;

                                if (image_info.size() == 1)
                                {
                                    mGoogleMap.addMarker(new MarkerOptions().anchor(0, 0)
                                            .position(exifLatLng)
                                            .title("0"))
                                            .setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.color(Color.rgb(255, 113, 181));
                                    polylineOptions.width(10);
                                    polylineOptions.pattern(PATTERN_POLYGON_ALPHA);
                                    arrayPoints.add(exifLatLng);
                                    polylineOptions.addAll(arrayPoints);
                                    polyline.add( mGoogleMap.addPolyline(polylineOptions));
                                    cnt = 0;
                                    //cnt2= cnt2+1;
                                }

                                else if (image_info.size()>1) {
                                    info1 = image_info.get(cnt).split(":");
                                    int k = image_info.size() - 1;
                                    info2 = image_info.get(k).split(":");

                                    float[] results = new float[1];
                                    Location.distanceBetween(Float.parseFloat(info1[1]), Float.parseFloat(info1[2]), Float.parseFloat(info2[1]), Float.parseFloat(info2[2]), results);
                                    Location.distanceBetween(Float.parseFloat(info1[1]), Float.parseFloat(info1[2]), Float.parseFloat(info2[1]), Float.parseFloat(info2[2]), results);
                                    float result = results[0];
                                    //Toast.makeText(MapsActivity.this, result + "m", Toast.LENGTH_SHORT).show();
                                    if(result >= 500)
                                    {
                                        mGoogleMap.addMarker(new MarkerOptions().anchor(0, 0)
                                                .position(exifLatLng)
                                                .title(""+(cnt2+1)))
                                                .setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
                                        polylineOptions = new PolylineOptions();
                                        polylineOptions.color(Color.rgb(255, 113, 181));
                                        polylineOptions.width(10);
                                        polylineOptions.pattern(PATTERN_POLYGON_ALPHA);
                                        arrayPoints.add(exifLatLng);
                                        polylineOptions.addAll(arrayPoints);
                                        polyline.add( mGoogleMap.addPolyline(polylineOptions));
                                        marker_list.add(cnt+":"+k);
                                        cnt = image_info.size()-1;
                                        cnt2 =cnt2+1;
                                    }
                                   /* else if(images.size() == cnt2+1)
                                    {
                                        marker_list.add(cnt+":"+k);
                                    }*/
                                }
                            }
                        }
                    }
                    marker_list.add(cnt+":"+(cnt3));

                    Intent intent = new Intent(MapsActivity.this, ImageGridActivity.class);
                    //intent.putStringArrayListExtra("image_info",image_info);
                    //startActivity(intent);
                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            int index_num = Integer.parseInt(marker.getTitle());
                            String s_temp = marker_list.get(index_num);
                            String[] temp = s_temp.split(":");
                            int num_temp0 = Integer.parseInt(temp[0]);
                            int num_temp1 = Integer.parseInt(temp[1]);

                            ArrayList<String> temp_array= new ArrayList<String>();
                            temp_array.clear();
                            if((marker_list.size()-1) == index_num) {
                                //Toast.makeText(MapsActivity.this, "나다", Toast.LENGTH_SHORT).show();
                                if (num_temp0 == num_temp1) {
                                    temp_array.add(image_info.get(image_info.size()-1));

                                }
                                else {
                                    for(int i=num_temp0;i<=(image_info.size()-1);i++){
                                        temp_array.add(image_info.get(i));
                                    }
                                }
                            }
                            else {
                                for (int i = num_temp0; i <= num_temp1-1; i++) {
                                    temp_array.add(image_info.get(i));
                                }
                            }
                            String title_name = Integer.toString(index_num);
                            //Toast.makeText(MapsActivity.this, title_name, Toast.LENGTH_SHORT).show();
                            intent.putExtra("marker_title",title_name);
                            intent.putStringArrayListExtra("image_info",temp_array);
                            startActivity(intent);
                            return false;
                        }
                    });
                }











                ///***************************************************************************************
                // 위에는 전체일정
                // asdasd
                // 아래는 날짜선택
                //****************************************************************************************









                else
                {
                    String loop_date;
                    loop_date = select_date;
                    float[] out_latlong;

                    images = getPathOfAllImages(loop_date);

                    for (String string : images) {

                        //String path_pho = images.get(0);
                        //Uri uri_pho = getUriFromPath(path_pho);
                        out_latlong = showExif(string);
                        if (out_latlong[0] != 0 && out_latlong[1] != 0) {
                            cnt3 = cnt3 +1;
                            image_info.add(string + ":" + Float.toString(out_latlong[0]) + ":" + Float.toString(out_latlong[1]) + ":" + select_date);
                            //Toast.makeText(context, images.toString(), Toast.LENGTH_SHORT).show();
                            LatLng exifLatLng = new LatLng(out_latlong[0],out_latlong[1]);
                            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                            Bitmap bmp = Bitmap.createBitmap(170, 170, conf);
                            Canvas canvas1 = new Canvas(bmp);

                            canvas1.drawColor(Color.WHITE);
                            Paint color = new Paint();
                            color.setTextSize(35);
                            color.setColor(Color.BLACK);
                            Bitmap resize_bmp = resizeBitmapImg(BitmapFactory.decodeFile(photoPath));

                            try {
                                // 이미지를 상황에 맞게 회전시킨다
                                ExifInterface exif = new ExifInterface(photoPath);
                                int exifOrientation = exif.getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                int exifDegree = exifOrientationToDegrees(exifOrientation);

                                Matrix mat = new Matrix();
                                mat.postRotate(exifDegree);
                                resize_bmp = Bitmap.createBitmap(resize_bmp, 0, 0,resize_bmp.getWidth(),resize_bmp.getHeight(), mat, true);


                            } catch (Exception e) {

                            }
                            canvas1.drawBitmap(resize_bmp, resize_bmp.getHeight()*(1/20)+10, resize_bmp.getWidth()*(1/20)+10, null);

                            String [] info1;
                            String [] info2;


                            if (image_info.size() == 1)
                            {
                                mGoogleMap.addMarker(new MarkerOptions().anchor(0, 0)
                                        .position(exifLatLng)
                                        .title("0"))
                                        .setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
                                polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.rgb(255, 113, 181));
                                polylineOptions.width(10);
                                polylineOptions.pattern(PATTERN_POLYGON_ALPHA);
                                arrayPoints.add(exifLatLng);
                                polylineOptions.addAll(arrayPoints);
                                polyline.add( mGoogleMap.addPolyline(polylineOptions));
                                cnt = 0;
                            }

                            else if (image_info.size()>1) {
                                info1 = image_info.get(cnt).split(":");
                                int k = image_info.size() - 1;
                                info2 = image_info.get(k).split(":");

                                float[] results = new float[1];
                                Location.distanceBetween(Float.parseFloat(info1[1]), Float.parseFloat(info1[2]), Float.parseFloat(info2[1]), Float.parseFloat(info2[2]), results);
                                Location.distanceBetween(Float.parseFloat(info1[1]), Float.parseFloat(info1[2]), Float.parseFloat(info2[1]), Float.parseFloat(info2[2]), results);
                                float result = results[0];
                                //Toast.makeText(MapsActivity.this, result + "m", Toast.LENGTH_SHORT).show();
                                if(result >= 500)
                                {
                                    mGoogleMap.addMarker(new MarkerOptions().anchor(0, 0)
                                            .position(exifLatLng)
                                            .title(""+(cnt2+1)))
                                            .setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.color(Color.rgb(255, 113, 181));
                                    polylineOptions.width(10);
                                    polylineOptions.pattern(PATTERN_POLYGON_ALPHA);
                                    arrayPoints.add(exifLatLng);
                                    polylineOptions.addAll(arrayPoints);
                                    polyline.add( mGoogleMap.addPolyline(polylineOptions));

                                    marker_list.add(cnt+":"+k);
                                    cnt = image_info.size()-1;
                                    cnt2 =cnt2+1;



                                }
                            }


                        }
                    }
                    marker_list.add(cnt+":"+(cnt3));


                    Intent intent = new Intent(MapsActivity.this, ImageGridActivity.class);
                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            //Toast.makeText(context, marker.getTitle(), Toast.LENGTH_SHORT).show();
                            int index_num = Integer.parseInt(marker.getTitle());
                            String[] temp = marker_list.get(index_num).split(":");
                            int num_temp0 = Integer.parseInt(temp[0]);
                            int num_temp1 = Integer.parseInt(temp[1]);

                            ArrayList<String> temp_array= new ArrayList<String>();
                            temp_array.clear();
                            if((marker_list.size()-1) == index_num) {
                                //Toast.makeText(MapsActivity.this, "나다", Toast.LENGTH_SHORT).show();
                                if (num_temp0 == num_temp1) {
                                    temp_array.add(image_info.get(image_info.size()-1));

                                }
                                else {
                                    for(int i=num_temp0;i<=(image_info.size()-1);i++){
                                        temp_array.add(image_info.get(i));
                                    }
                                }
                            }
                            else {
                                for (int i = num_temp0; i <= num_temp1-1; i++) {
                                    temp_array.add(image_info.get(i));
                                }
                            }
                            String title_name = Integer.toString(index_num);
                            //Toast.makeText(MapsActivity.this, title_name, Toast.LENGTH_SHORT).show();
                            intent.putExtra("marker_title",title_name);
                            intent.putStringArrayListExtra("image_info",temp_array);
                            startActivity(intent);
                            return false;
                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mGoogleMap != null)
                mGoogleMap.setMyLocationEnabled(true);
        }
    }

    static public Bitmap resizeBitmapImg(Bitmap original) {

        int resizeWidth = 150;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }




    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (flag != 1) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
            flag = 1;
        }
    }

    public void setDefaultLocation() {

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요";
        if (currentMarker != null) currentMarker.remove();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;

    }

    //* ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {
                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
        switch (permsRequestCode) {
            case RQS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grandResults.length > 0
                        && grandResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //showExif(targetUri);
                } else {
                    Toast.makeText(this,
                            "permission denied!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Uri dataUri = data.getData();

            if (requestCode == RQS_OPEN_IMAGE) {
                targetUri = dataUri;
            }
        }

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

    private boolean CheckPermission_READ_EXTERNAL_STORAGE() {
        // return true: have permission
        // return false: no permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    RQS_READ_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }
}