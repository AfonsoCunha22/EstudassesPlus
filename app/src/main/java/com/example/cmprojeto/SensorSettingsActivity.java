package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class SensorSettingsActivity extends AppCompatActivity {
    ImageView goBack;
    SwitchCompat tempSwitch, lightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);

        tempSwitch = (SwitchCompat) findViewById(R.id.tempSwitch);
        lightSwitch = (SwitchCompat) findViewById(R.id.lightSwitch);
        goBack = (ImageView) findViewById(R.id.goBack);

        SharedPreferences preferences = getSharedPreferences("sensorSettings", MODE_PRIVATE);

        if(preferences.getBoolean("isFirstRun", true)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("isFirstRun", false);
            edit.putBoolean("tempSwitch", true);
            edit.putBoolean("lightSwitch", true);

            edit.apply();
        } else {
            tempSwitch.setChecked(preferences.getBoolean("tempSwitch", true));
            lightSwitch.setChecked(preferences.getBoolean("lightSwitch", true));
        }

        tempSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("tempSwitch", tempSwitch.isChecked());
            edit.apply();
        });

        lightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("lightSwitch", lightSwitch.isChecked());
            edit.apply();
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }
}