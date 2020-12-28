package com.example.cmprojeto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewSessionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {
    Button confirm;
    ImageView timeP, dateP, locationP;
    TextView selectedTime,selectedDate, selectedLocation;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        bundle = getIntent().getExtras();
        confirm = (Button) findViewById(R.id.confirm_button);
        timeP = (ImageView) findViewById(R.id.timeButton);
        dateP = (ImageView) findViewById(R.id.dateButton);
        locationP = (ImageView) findViewById(R.id.locationButton);
        selectedTime = (TextView) findViewById(R.id.selectedTime);
        selectedDate = (TextView) findViewById(R.id.selectedDate);
        selectedLocation=(TextView) findViewById(R.id.selectedLocal);
        selectedTime.setText(R.string.pls_time);
        selectedDate.setText(R.string.pls_date);
        
        //if(bundle.containsKey("latitude") && bundle.containsKey("longitude")){
        //    selectedLocation.setText(bundle.getDouble("latitude")+" : "+bundle.getDouble("longitude"));
        //}else{
        //    selectedLocation.setText((R.string.pls_local));
        //}
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
                startActivity(intent);
            }
        });
        timeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        dateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        locationP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String aux = R.string.selected_time_label + hourOfDay + ":" + minute;
        selectedTime.setText(aux);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String aux = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        selectedDate.setText(aux);
    }

}
