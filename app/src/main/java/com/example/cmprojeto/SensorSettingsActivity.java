package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

public class SensorSettingsActivity extends AppCompatActivity {
    ImageView goBack;
    SwitchCompat accelerometerSwitch, lightSwitch;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);

        accelerometerSwitch = (SwitchCompat) findViewById(R.id.accelerometer);
        lightSwitch = (SwitchCompat) findViewById(R.id.lightSwitch);
        goBack = (ImageView) findViewById(R.id.goBack);

        preferences = getSharedPreferences("sensorSettings", MODE_PRIVATE);
        setupSharedPreferences();

        accelerometerSwitch.setOnCheckedChangeListener((compoundButton, b) -> updatePreferences());
        lightSwitch.setOnCheckedChangeListener((compoundButton, b) -> updatePreferences());

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setupSharedPreferences() {
        accelerometerSwitch.setChecked(preferences.getBoolean("accelerometerSwitch", true));
        lightSwitch.setChecked(preferences.getBoolean("lightSwitch", true));
    }

    private void updatePreferences() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("lightSwitch", lightSwitch.isChecked());
        edit.putBoolean("accelerometerSwitch", accelerometerSwitch.isChecked());

        edit.apply();
    }
}