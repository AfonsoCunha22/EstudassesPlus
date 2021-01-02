package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

public class NotificationSettingsActivity extends AppCompatActivity {
    SwitchCompat sessionStart;
    SwitchCompat studyStart;
    SwitchCompat studyBreak;
    SwitchCompat studyEnd;

    ImageView goBack;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        sessionStart = (SwitchCompat) findViewById(R.id.sessionStart);
        studyStart = (SwitchCompat) findViewById(R.id.studyStart);
        studyBreak = (SwitchCompat) findViewById(R.id.studyBreak);
        studyEnd = (SwitchCompat) findViewById(R.id.studyEnd);
        goBack = (ImageView) findViewById(R.id.goBack);

        preferences = getSharedPreferences("notificationSettings", MODE_PRIVATE);
        setupSharedPreferences();

        sessionStart.setOnCheckedChangeListener((compoundButton, b) -> updatePreferences());
        studyStart.setOnCheckedChangeListener((compoundButton, b) -> updatePreferences());
        studyBreak.setOnCheckedChangeListener((compoundButton, b) -> updatePreferences());
        studyEnd.setOnCheckedChangeListener((compoundButton, b) -> updatePreferences());

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setupSharedPreferences() {
        sessionStart.setChecked(preferences.getBoolean("sessionStart", true));
        studyStart.setChecked(preferences.getBoolean("studyStart", true));
        studyBreak.setChecked(preferences.getBoolean("studyBreak", true));
        studyEnd.setChecked(preferences.getBoolean("studyEnd", true));
    }

    private void updatePreferences() {
        SharedPreferences.Editor edit = preferences.edit();

        edit.putBoolean("sessionStart", sessionStart.isChecked());
        edit.putBoolean("studyStart", studyStart.isChecked());
        edit.putBoolean("studyBreak", studyBreak.isChecked());
        edit.putBoolean("studyEnd", studyEnd.isChecked());

        edit.apply();
    }
}