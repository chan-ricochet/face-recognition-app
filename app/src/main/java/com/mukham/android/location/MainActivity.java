package com.mukham.android.location;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    Button b,b2;
    String lat;
    Criteria criteria= new Criteria();
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public boolean locationPermissionGranted = false;
    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    Location location;
    float distanceInMeters;
    boolean isWithin100m;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = (TextView) findViewById(R.id.textview1);
        b = findViewById(R.id.button);
        b2 = findViewById(R.id.button3);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), Activity2.class);
                startActivity(activity2Intent);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location!=null){
                Uri l = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude());
                showMap(l);}
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getDeviceLocation();

    }


    private void getDeviceLocation() {
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);


        try {
            while(!locationPermissionGranted){
            locationPermissionManager();}
            locationManager.requestLocationUpdates(provider, 0, 0, this);
            location = locationManager.getLastKnownLocation(provider);
            if(location!=null) {
                txtLat.setText("Your current location:\n" +
                    "Latitude:" + location.getLatitude() +
                    ", Longitude:" + location.getLongitude());
                LocationVerify();
            }
            else {
                txtLat.setText("Error connecting to location");
            }
        }
        catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }

    public void locationPermissionManager(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }

    public void LocationVerify() {
        Location center=new Location("dummyprovider");
        center.setLatitude(16.4963);
        center.setLongitude(80.5007);
        distanceInMeters = location.distanceTo(center);
        isWithin100m = distanceInMeters < 100;
        if(isWithin100m) {
            Toast.makeText(getApplicationContext(),"Location verified", Toast.LENGTH_SHORT).show();

        }
        else {Toast.makeText(getApplicationContext(),"Not at VIT-AP", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onLocationChanged(Location location) {
        txtLat = (TextView) findViewById(R.id.textview1);
        if(location!=null) {
            txtLat.setText("Your current location:\n" +
                    "Latitude:" + location.getLatitude() +
                    ", Longitude:" + location.getLongitude());
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
