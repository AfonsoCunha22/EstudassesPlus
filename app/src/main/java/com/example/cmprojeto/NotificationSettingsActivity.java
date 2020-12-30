package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class NotificationSettingsActivity extends AppCompatActivity {

    SwitchCompat sessionStart;
    SwitchCompat studyStart;
    SwitchCompat studyBreak;
    SwitchCompat studyEnd;

    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        sessionStart = (SwitchCompat) findViewById(R.id.sessionStart);
        studyStart = (SwitchCompat) findViewById(R.id.studyStart);
        studyBreak = (SwitchCompat) findViewById(R.id.studyBreak);
        studyEnd = (SwitchCompat) findViewById(R.id.studyEnd);
        goBack = (ImageView) findViewById(R.id.goBack);

        SharedPreferences preferences = getSharedPreferences("notificationSettings", MODE_PRIVATE);

        if(preferences.getBoolean("isFirstRun", true)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("isFirstRun", false);
            edit.putBoolean("sessionStart", true);
            edit.putBoolean("studyStart", true);
            edit.putBoolean("studyBreak", true);
            edit.putBoolean("studyEnd", true);

            edit.apply();
        } else {
            sessionStart.setChecked(preferences.getBoolean("sessionStart", true));
            studyStart.setChecked(preferences.getBoolean("studyStart", true));
            studyBreak.setChecked(preferences.getBoolean("studyBreak", true));
            studyEnd.setChecked(preferences.getBoolean("studyEnd", true));
        }

        sessionStart.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("sessionStart", sessionStart.isChecked());
            edit.apply();
        });

        studyStart.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("studyStart", studyStart.isChecked());
            edit.apply();
        });

        studyBreak.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("studyBreak", studyBreak.isChecked());
            edit.apply();
        });

        studyEnd.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("studyEnd", studyEnd.isChecked());
            edit.apply();
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }
}