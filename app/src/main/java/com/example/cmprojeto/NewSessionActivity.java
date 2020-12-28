package com.example.cmprojeto;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class NewSessionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Button confirm;
    ImageView timeP;
    TextView selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        confirm = (Button) findViewById(R.id.confirm_button);
        //timeP = (ImageView) findViewById(R.id.timeButton);
        //selectedTime = (TextView) findViewById(R.id.selectedTime);
        selectedTime.setText(R.string.pls_time);
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
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String aux = R.string.selected_time_label + hourOfDay + ":" + minute;
        selectedTime.setText(aux);
    }
}
