package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.cmprojeto.callbacks.SessionCallback;
import com.example.cmprojeto.callbacks.SingleSessionCallback;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Session;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExpandedSessionActivity extends AppCompatActivity {
    DBHelper dbHelper = DBHelper.getInstance();
    TextView subjectLabel, userLabel, locationLabel, dateTimeLabel, descriptionLabel;
    Button enrollButton;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_session);
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        subjectLabel = findViewById(R.id.subjectLabelSession);
        userLabel=findViewById(R.id.userLabel);
        locationLabel=findViewById(R.id.locationLabel);
        dateTimeLabel=findViewById(R.id.dateTimeLabel);
        descriptionLabel=findViewById(R.id.descriptionText);
        enrollButton=findViewById(R.id.enrollButton);
        dbHelper.getSessionByID(getIntent().getExtras().getString("sessionID"), session -> {
            dbHelper.getUserUsername(session.getUserID(), username -> {
                userLabel.setText(username);
            });
            subjectLabel.setText(session.getSubject());
            locationLabel.setText(getLocationFromLarLong(session.getLocation().latitude, session.getLocation().longitude));
            dateTimeLabel.setText(sdf.format(session.getDateTime()));
            descriptionLabel.setText(session.getDescription());
                });

    }
    private String getLocationFromLarLong(Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                return listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Inválido";
    }
}