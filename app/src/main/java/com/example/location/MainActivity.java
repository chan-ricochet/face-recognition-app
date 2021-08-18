package com.example.location;

import android.location.Criteria;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.location.databinding.ActivityMainBinding;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    Criteria criteria= new Criteria();
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public boolean locationPermissionGranted = false;
    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    Location location=null;
    float distanceInMeters;
    boolean isWithin100m;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = (TextView) findViewById(R.id.textview1);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);


        try {
            locationPermissionManager();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            location = locationManager.getLastKnownLocation(provider);
            txtLat.setText("Your current location:\n" +
                    "Latitude:" + location.getLatitude() +
                    ", Longitude:" + location.getLongitude());
        }
        catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage(), e);
        }
        LocationVerify();
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
            Intent intent = new Intent(this, Camera.class);

            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
        }
        else {Toast.makeText(getApplicationContext(),"Not at VIT-AP", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onLocationChanged(Location location) {
        txtLat = (TextView) findViewById(R.id.textview1);
        txtLat.setText("Your current location:\n" +
                "Latitude:" + location.getLatitude() +
                ", Longitude:" + location.getLongitude());
    }
}
