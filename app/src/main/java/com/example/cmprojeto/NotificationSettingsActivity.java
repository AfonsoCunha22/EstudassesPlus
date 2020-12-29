package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;

public class NotificationSettingsActivity extends AppCompatActivity {

    SwitchCompat sessionStart;
    SwitchCompat studyStart;
    SwitchCompat studyBreak;
    SwitchCompat studyEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        sessionStart = (SwitchCompat) findViewById(R.id.sessionStart);
        studyStart = (SwitchCompat) findViewById(R.id.studyStart);
        studyBreak = (SwitchCompat) findViewById(R.id.studyBreak);
        studyEnd = (SwitchCompat) findViewById(R.id.studyEnd);

        //TODO Set the starting status to the saved settings in DB

        sessionStart.setChecked(true);
        studyStart.setChecked(true);
        studyBreak.setChecked(true);
        studyEnd.setChecked(true);

        sessionStart.setOnClickListener(v -> {
            //TODO Update user account in DB
            Log.d("STATE", "Session Start: " + sessionStart.isChecked());
        });

        studyStart.setOnClickListener(v -> {
            //TODO Update user account in
            Log.d("STATE", "Study Start: " + studyStart.isChecked());
        });

        studyBreak.setOnClickListener(v -> {
            //TODO Update user account in DB
            Log.d("STATE", "Study Break: " + studyBreak.isChecked());
        });

        studyEnd.setOnClickListener(v -> {
            //TODO Update user account in DB
            Log.d("STATE", "Study End: " + studyEnd.isChecked());
        });
    }
}