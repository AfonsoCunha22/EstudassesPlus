package com.example.cmprojeto;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;

import com.example.cmprojeto.database.DBHelper;

public class PlanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Break = "Breaktime";
    private static final String Duration = "Duration";
    private static final String Subject = "Subject";
    private static final String ColorSt = "Color";

    private TextView subject, duration, breakTime;
    private Button colorButton, deleteButton;
    private String planID;

    // TODO: Rename and change types of parameters
    private String mBreakTime;
    private String mDuration;
    private String mSubjectName;
    private String mColor;


    DBHelper dbHelper = DBHelper.getInstance();
    public PlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanFragment newInstance(String breakTime, String duration, String subject, String color) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putString(Break, breakTime);
        args.putString(Duration, duration);
        args.putString(Subject, subject);
        args.putString(ColorSt, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBreakTime = getArguments().getString(Break);
            mDuration = getArguments().getString(Duration);
            mSubjectName= getArguments().getString(Subject);
            mColor = getArguments().getString(ColorSt);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plan, container, false);
        subject = v.findViewById(R.id.subjectName);
        duration = v.findViewById(R.id.planTime);
        breakTime = v.findViewById(R.id.planBreak);
        colorButton = v.findViewById(R.id.planColor);
        deleteButton = v.findViewById(R.id.deleteButton);
        subject.setText(getArguments().getString(Subject));
        duration.setText(getArguments().getString(Duration));
        breakTime.setText(getArguments().getString(Break));
        colorButton.setBackgroundColor(Color.parseColor((getArguments().getString(ColorSt))));


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}