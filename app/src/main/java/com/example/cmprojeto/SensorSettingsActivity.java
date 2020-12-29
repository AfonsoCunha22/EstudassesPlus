package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;

public class SensorSettingsActivity extends AppCompatActivity {
    ImageView goBack;
    SwitchCompat tempSwitch, lightSwitch;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);

        goBack = (ImageView) findViewById(R.id.goBack);

        tempSwitch = (SwitchCompat) findViewById(R.id.tempSwitch);
        lightSwitch = (SwitchCompat) findViewById(R.id.lightSwitch);

        dbHelper = new DBHelper();

        tempSwitch.setOnClickListener(v -> {

        });

        lightSwitch.setOnClickListener(v -> {

        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

    }
}