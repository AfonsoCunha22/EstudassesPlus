package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cmprojeto.database.DBHelper;

public class ExpandedSessionActivity extends AppCompatActivity {
    DBHelper dbHelper = DBHelper.getInstance();
    TextView subjectLabel, userLabel, locationLabel, dateTimeLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_session);
        subjectLabel = findViewById(R.id.subjectLabelSession);
    }
}