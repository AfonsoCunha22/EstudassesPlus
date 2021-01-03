package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class LanguageSettingsActivity extends AppCompatActivity {

    ImageView goBack;
    Spinner spinner;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);

        spinner = (Spinner) findViewById(R.id.languageSpinner);
        goBack = (ImageView) findViewById(R.id.goBack);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        preferences = getSharedPreferences("languageSettings", MODE_PRIVATE);
        setupSharedPreferences();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("selectedLanguage", parent.getItemAtPosition(position).toString().split(" ")[2]);

                edit.apply();

                //TODO - Change the display language of the application
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setupSharedPreferences() {
        String selectedLanguage = preferences.getString("selectedLanguage",  "PT");
        
        if(selectedLanguage.equals("EN"))
            spinner.setSelection(1); // English Index
        else
            spinner.setSelection(0); // Portuguese Index
    }
}