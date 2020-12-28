package com.example.cmprojeto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class NewSessionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {
    Button confirm;
    ImageView timeP, dateP;
    TextView selectedTime,selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        confirm = (Button) findViewById(R.id.confirm_button);
        timeP = (ImageView) findViewById(R.id.timeButton);
        dateP = (ImageView) findViewById(R.id.dateButton);
        selectedTime = (TextView) findViewById(R.id.selectedTime);
        selectedDate = (TextView) findViewById(R.id.selectedDate);
        selectedTime.setText(R.string.pls_time);
        selectedDate.setText(R.string.pls_date);
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
