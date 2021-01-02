package com.example.cmprojeto;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
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

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.database.DBHelper;

public class PlanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Duration = "Duration";
    private static final String Subject = "Subject";
    private static final String ColorSt = "Color";
    private static final String PlanID = "PlanID";

    private TextView subject, duration;
    private Button colorButton, deleteButton;
    private String planID;

    // TODO: Rename and change types of parameters
    private String mDuration;
    private String mSubjectName;
    private String mColor;


    DBHelper dbHelper = DBHelper.getInstance();
    FragmentClick clickInterface;

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
    public static PlanFragment newInstance(String duration, String subject, String color, String planID) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putString(Duration, duration);
        args.putString(Subject, subject);
        args.putString(ColorSt, color);
        args.putString(PlanID, planID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDuration = getArguments().getString(Duration);
            mSubjectName= getArguments().getString(Subject);
            mColor = getArguments().getString(ColorSt);
        }
    }

    public void setClickInterface(FragmentClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plan, container, false);
        subject = v.findViewById(R.id.subjectName);
        duration = v.findViewById(R.id.planTime);
        colorButton = v.findViewById(R.id.planColor);
        deleteButton = v.findViewById(R.id.deleteButton);
        subject.setText(getArguments().getString(Subject));
        duration.setText(getArguments().getString(Duration));
        colorButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor((getArguments().getString(ColorSt)))));
        planID = getArguments().getString(PlanID);

        colorButton.setOnClickListener(v1 -> {
            clickInterface.buttonClicked(planID);
        });

        deleteButton.setOnClickListener(l->{
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(l.getContext());
            confirmDialog.setTitle(getResources().getString(R.string.confirm));
            confirmDialog.setMessage(getResources().getString(R.string.confirm_delete_plan));
            confirmDialog.setPositiveButton(getText(R.string.yes), (dialog, which) -> {
                dbHelper.deletePlan(planID, plans -> {
                });
                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            });
            confirmDialog.setNegativeButton(getText(R.string.no),(dialog, which) -> { });
            confirmDialog.create().show();
        });


        return v;
    }
}