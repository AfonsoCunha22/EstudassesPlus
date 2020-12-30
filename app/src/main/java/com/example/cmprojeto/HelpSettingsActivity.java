package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class HelpSettingsActivity extends AppCompatActivity {

    ImageView goBack;
    EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_settings);

        goBack = (ImageView) findViewById(R.id.goBack);
        emailField = (EditText) findViewById(R.id.emailField);

        emailField.setEnabled(false);
        emailField.setText(getResources().getString(R.string.estudasses_email));

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }
}