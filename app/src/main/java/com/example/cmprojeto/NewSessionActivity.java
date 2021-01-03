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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.cmprojeto.callbacks.BooleanCallback;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.fragments.DatePickerFragment;
import com.example.cmprojeto.fragments.TimePickerFragment;
import com.example.cmprojeto.model.Session;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.type.DateTime;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewSessionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {
    Button confirm, addSubjectBtn;
    ImageView timeP, dateP, locationP, goBack;
    TextView selectedTime,selectedDate, selectedLocation;
    Bundle receiveBundle, sendBundle;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText description;
    DBHelper dbHelper = DBHelper.getInstance();
    int  minute,hour;
    Calendar cal;
    Double latitude, longitude;
    ArrayAdapter<String> subjectsAdapter;
    SearchableSpinner subjectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        receiveBundle = getIntent().getExtras();
        sendBundle = new Bundle();
        subjectsAdapter = new ArrayAdapter<>(NewSessionActivity.this, android.R.layout.simple_spinner_dropdown_item, DBHelper.SUBJECT_LIST.getSubjects());
        addSubjectBtn = (Button) findViewById(R.id.addButton);
        description = (EditText) findViewById(R.id.editDescription);
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
        cal=Calendar.getInstance();
        subjectSpinner = (SearchableSpinner) findViewById(R.id.editSubject);
        subjectSpinner.setAdapter(subjectsAdapter);

        populateActivity();
        
        if(receiveBundle != null){
            selectedLocation.setText(getLocationFromLarLong(receiveBundle.getDouble("latitude"), receiveBundle.getDouble("longitude")));
            this.latitude=receiveBundle.getDouble("latitude");
            this.longitude=receiveBundle.getDouble("longitude");
        }else{
            selectedLocation.setText((R.string.pls_local));
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.createSession(new Session(subjectSpinner.getSelectedItem().toString(), cal.getTime(), new Time((hour*21600)+(minute*360)),new LatLng(latitude,longitude), description.getText().toString()),
                        result -> {
                            if(result){
                                Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
                                startActivity(intent);
                            }
                        });

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
        addSubjectBtn.setOnClickListener(v -> {
            EditText newSubject = new EditText(v.getContext());
            AlertDialog.Builder addSubjectDialog = new AlertDialog.Builder(v.getContext());
            addSubjectDialog.setTitle(getResources().getString(R.string.new_subject));
            addSubjectDialog.setMessage(getResources().getString(R.string.insert_subject_added));
            addSubjectDialog.setView(newSubject);

            addSubjectDialog.setPositiveButton(getResources().getString(R.string.add), (dialog, which) -> {
                String subjectName = newSubject.getText().toString();
                if(subjectName.trim().equals("")){
                    Toast.makeText(this, getText(R.string.subject_err),
                            Toast.LENGTH_LONG).show();
                }else if(subjectName.trim().length() > 15){
                    Toast.makeText(this, getText(R.string.subject_too_long),
                            Toast.LENGTH_LONG).show();
                }
                else {
                    dbHelper.createSubject(subjectName, callback -> {
                        subjectsAdapter.add(subjectName.trim());
                        subjectsAdapter.setNotifyOnChange(true);
                    });
                }
            });

            addSubjectDialog.setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {});

            addSubjectDialog.create().show();
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String aux = R.string.selected_time_label + hourOfDay + ":" + minute;
        this.hour=hourOfDay/1000;
        this.minute=minute/(60*1000);
        selectedTime.setText(aux);
    }
    public void populateActivity(){
        if(!DBHelper.SUBJECT_LIST.isPopulated()){
            dbHelper.getSubjects(subjects -> {
                DBHelper.SUBJECT_LIST.populate(subjects);
            });
        }else {
            dbHelper.getSubjects(subjects -> {
                for (String subject : subjects){
                    if(!DBHelper.SUBJECT_LIST.getSubjects().contains(subject)){
                        DBHelper.SUBJECT_LIST.getSubjects().add(subject);
                    }
                }
            });
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        cal.set(year,month,dayOfMonth);
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
