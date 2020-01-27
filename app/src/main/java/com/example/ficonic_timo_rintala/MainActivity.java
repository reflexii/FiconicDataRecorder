package com.example.ficonic_timo_rintala;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
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

    //StartAccelerometer
    private float currentAccelX = 0f;
    private float currentAccelY = 0f;
    private float currentAccelZ = 0f;

    private boolean startClicked = false;

    private SensorManager sensorManager;

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
                startClicked = true;

                StartAccelerometer();
                EnableTimeStamp();
            }
        });

        //Setup stop button
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClicked = false;
                StopAccelerometer();
            }
        });


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    public void StartAccelerometer() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void StopAccelerometer(){
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
