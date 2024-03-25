package com.example.gps_speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private TextView speedTextView;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedTextView = findViewById(R.id.speedTextView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // GPS izni kontrolü
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            startLocationUpdates();

            // SpeedometerNeedle'ı bul ve başlangıç hızını set et
            SpeedometerNeedle speedometerNeedle = findViewById(R.id.speedometerNeedle);
            speedometerNeedle.setSpeed(0);
        }
    }

    private void startLocationUpdates() {
        LocationListener locationListener = new LocationListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onLocationChanged(@NonNull Location location) {
                float speed = location.getSpeed(); // Speed in m/s
                double speedKMH = speed * 3.6; // Convert speed to km/h
                speedTextView.setText("Hız: " + String.format("%.1f", speedKMH) + " km/s");

                // Update the speedometer needle
                SpeedometerNeedle speedometerNeedle = findViewById(R.id.speedometerNeedle);
                speedometerNeedle.setSpeed(speed);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };

        // GPS güncellemelerini iste
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, // minimum zaman aralığı (ms)
                    1, // minimum mesafe aralığı (metre)
                    locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }
}