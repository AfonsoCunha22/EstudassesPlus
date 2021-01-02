package com.example.cmprojeto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Color;
import com.example.cmprojeto.model.Plan;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.sql.SQLOutput;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewPlan extends AppCompatActivity {

    ImageView goBack;
    EditText description;
    Button createPlan, addSubjectBtn;
    NumberPicker durationPicker;
    SearchableSpinner colorSpinner, subjectSpinner;

    DBHelper dbHelper = DBHelper.getInstance();
    List<String> colors = new ArrayList<>();
    ArrayAdapter<String> subjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);
        populateActivity();
        subjectsAdapter = new ArrayAdapter<>(NewPlan.this, android.R.layout.simple_spinner_dropdown_item, DBHelper.SUBJECT_LIST.getSubjects());
        goBack = (ImageView) findViewById(R.id.goBack);
        description = (EditText) findViewById(R.id.editDescription);
        createPlan = (Button) findViewById(R.id.addPlanBtn);
        addSubjectBtn = (Button) findViewById(R.id.addSubjectBtn);

        durationPicker = (NumberPicker) findViewById(R.id.durationPicker);
        colorSpinner = (SearchableSpinner) findViewById(R.id.colorSpinner);
        subjectSpinner = (SearchableSpinner) findViewById(R.id.subjectSpinner);

        for (Color color: Color.values()) {
            colors.add(Color.toName(color));
        }

        durationPicker.setMinValue(50);
        durationPicker.setMaxValue(5000);
        durationPicker.setValue(120);

        colorSpinner.setAdapter(new ArrayAdapter<>(NewPlan.this, android.R.layout.simple_spinner_dropdown_item, colors));
        subjectSpinner.setAdapter(subjectsAdapter);
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
        goBack.setOnClickListener(v -> {
            AlertDialog.Builder exitConf = new AlertDialog.Builder(v.getContext());
            exitConf.setMessage(getResources().getString(R.string.exit_plan_confirmation));
            exitConf.setPositiveButton(getText(R.string.yes), (dialog, which) -> {});
            exitConf.setNegativeButton(getText(R.string.no), (dialog, which) -> {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(intent);
            });
            exitConf.create().show();

        });
        createPlan.setOnClickListener(v -> {
            if(colorSpinner.getSelectedItem() == null){
                AlertDialog.Builder colorError = new AlertDialog.Builder(v.getContext());
                colorError.setMessage(getResources().getString(R.string.color_err));
                colorError.setPositiveButton("Ok", (dialog, which) -> {});
                colorError.create().show();
            }else if(colorSpinner.getSelectedItem() == null){
                AlertDialog.Builder subError = new AlertDialog.Builder(v.getContext());
                subError.setMessage(getResources().getString(R.string.subject_err));
                subError.setPositiveButton("Ok", (dialog, which) -> {});
                subError.create().show();
            }else if (description.getText().toString().isEmpty()){
                description.setError(getText(R.string.desc_err));
            }else {
                dbHelper.createPlan(new Plan(subjectSpinner.getSelectedItem().toString(), description.getText().toString(), durationPicker.getValue(), Color.NameToObject(colorSpinner.getSelectedItem().toString()), false), complete ->{
                    if(complete){
                        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
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
}