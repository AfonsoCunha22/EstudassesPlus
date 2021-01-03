package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Locale;

public class LanguageSettingsActivity extends AppCompatActivity {

    ImageView goBack;
    Spinner spinner;
    String lang;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);

        lang = "";
        spinner = (Spinner) findViewById(R.id.languageSpinner);
        goBack = (ImageView) findViewById(R.id.goBack);
        confirm = (Button) findViewById(R.id.confirmBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("languagesSettings", MODE_PRIVATE);

        if(preferences.getBoolean("isFirstRun", true)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("isFirstRun", false);
            edit.putString("selectedLanguage", "EN");

            edit.apply();
        } else {
            String selectedLanguage = preferences.getString("selectedLanguage",  "EN");

            if(selectedLanguage.equals("EN")){
                spinner.setSelection(1);
                lang="en";
            }
            else{
                spinner.setSelection(0);
                lang="pt";
            }
        }

        confirm.setOnClickListener(v -> setLocale(lang));
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

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, HomeActivity.class);
        finish();
        startActivity(refresh);
    }
}