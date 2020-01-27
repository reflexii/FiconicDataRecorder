package com.example.ficonic_timo_rintala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    //Buttons
    private Button buttonStart;
    private Button buttonStop;

    //Texts
    private TextView text_Timestamp;
    private TextView text_Position;
    private TextView text_Bearing;
    private TextView text_Altitude;
    private TextView text_Speed;
    private TextView text_Accuracy;
    private TextView text_X;
    private TextView text_Y;
    private TextView text_Z;

    //Accelerometer
    private float currentAccelX = 0f;
    private float currentAccelY = 0f;
    private float currentAccelZ = 0f;

    //Location
    private LocationManager locationManager;

    private SensorManager sensorManager;

    //Request code
    public static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_Timestamp = findViewById(R.id.value_timestamp);
        text_Position = findViewById(R.id.value_position);
        text_Bearing = findViewById(R.id.value_bearing);
        text_Altitude = findViewById(R.id.value_altitude);
        text_Speed = findViewById(R.id.value_speed);
        text_Accuracy = findViewById(R.id.value_accuracy);
        text_X = findViewById(R.id.value_x);
        text_Y = findViewById(R.id.value_y);
        text_Z = findViewById(R.id.value_z);

        buttonStart = findViewById(R.id.button_Start);
        buttonStop = findViewById(R.id.button_Stop);

        //Setup start button
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartAccelerometer();
                EnableTimeStamp();
                EnableLocation();
            }
        });

        //Setup stop button
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopAccelerometer();
            }
        });


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    public void StartAccelerometer() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void StopAccelerometer() {
        sensorManager.unregisterListener(this);
    }

    //Change values on accelerometer
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            currentAccelX = event.values[0];
            currentAccelY = event.values[1];
            currentAccelZ = event.values[2];

            UpdateAccelerometerValues();
        }
    }

    //NOT NEEDED!
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void UpdateAccelerometerValues() {
        text_X.setText("X: " + currentAccelX);
        text_Y.setText("Y: " + currentAccelY);
        text_Z.setText("Z: " + currentAccelZ);
    }

    public void EnableTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
        text_Timestamp.setText(sdf.format(calendar.getTime()));
    }

    public void EnableLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, REQUEST_CODE);
                return;
            }
        } else {
            GpsButton();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                GpsButton();
                return;
        }
    }

    public void GpsButton() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        text_Position.setText("Position: " + "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
