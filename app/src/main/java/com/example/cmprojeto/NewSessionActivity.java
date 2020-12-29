package com.example.cmprojeto;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
    ImageView timeP, dateP, locationP, goBack;
    TextView selectedTime,selectedDate, selectedLocation;
    Bundle receiveBundle, sendBundle;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        receiveBundle = getIntent().getExtras();
        sendBundle = new Bundle();
        confirm = (Button) findViewById(R.id.confirm_button);
        timeP = (ImageView) findViewById(R.id.timeButton);
        dateP = (ImageView) findViewById(R.id.dateButton);
        locationP = (ImageView) findViewById(R.id.locationButton);
        selectedTime = (TextView) findViewById(R.id.selectedTime);
        selectedDate = (TextView) findViewById(R.id.selectedDate);
        selectedLocation=(TextView) findViewById(R.id.selectedLocal);
        selectedTime.setText(R.string.pls_time);
        selectedDate.setText(R.string.pls_date);
        goBack = (ImageView) findViewById(R.id.goBack);
        
        if(receiveBundle != null){
            selectedLocation.setText(getLocationFromLarLong(receiveBundle.getDouble("latitude"), receiveBundle.getDouble("longitude")));
        }else{
            selectedLocation.setText((R.string.pls_local));
        }

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
                getLocation();
            }
        });
        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
            startActivity(intent);
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
    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(NewSessionActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location !=null) {
                        Geocoder geocoder = new Geocoder(NewSessionActivity.this,
                                Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            sendBundle.putDouble("curLatitude",location.getLatitude());
                            System.out.println("macaco2-"+location.getLatitude());
                            sendBundle.putDouble("curLongitude",location.getLongitude());
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtras(sendBundle);
                            startActivity(intent);
                            //bundle.putString("localidade",addresses.get(0).getLocality());
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(NewSessionActivity.this
                    , new  String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }
    private String getLocationFromLarLong(Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                String _Location;
                return _Location = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Inválido";
    }

}
