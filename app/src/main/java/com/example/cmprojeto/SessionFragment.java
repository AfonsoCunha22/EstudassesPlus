package com.example.cmprojeto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.database.DBHelper;

public class SessionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DateTime = "DateTime";
    private static final String Subject = "Subject";
    private static final String User = "User";
    private static final String Location = "Location";
    private static final String SessionID = "SessionID";

    private TextView subject, dateTime, user, location;
    private Button  expandButton;
    private String sessionID;

    // TODO: Rename and change types of parameters
    private String mDateTime, mSubjectName, mLocation, mUser;


    DBHelper dbHelper = DBHelper.getInstance();
    FragmentClick clickInterface;

    public SessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SessionFragment newInstance(String dateTime, String user, String subject, String location, String sessionID) {
        SessionFragment fragment = new SessionFragment();
        Bundle args = new Bundle();
        args.putString(Location, location);
        args.putString(Subject, subject);
        args.putString(User, user);
        args.putString(DateTime, dateTime);
        args.putString(SessionID, sessionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = getArguments().getString(Location);
            mSubjectName= getArguments().getString(Subject);
            mUser = getArguments().getString(User);
            mDateTime = getArguments().getString(DateTime);
        }
    }

    public void setClickInterface(FragmentClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_session, container, false);
        subject = v.findViewById(R.id.subjectName);
        dateTime = v.findViewById(R.id.sessionDateTime);
        user = v.findViewById(R.id.sessionUser);
        location = v.findViewById(R.id.sessionLocation);
        expandButton = v.findViewById(R.id.expandSessionButton);
        subject.setText(getArguments().getString(Subject));
        dateTime.setText(getArguments().getString(DateTime));
        user.setText(getArguments().getString(User));
        location.setText(getArguments().getString(Location));

        sessionID = getArguments().getString(SessionID);
        expandButton.setOnClickListener(l->{
            clickInterface.buttonClicked(sessionID);
        });


        return v;
    }
}
