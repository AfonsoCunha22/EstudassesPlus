package com.example.cmprojeto;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.database.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private TextView textViewLogout;
    private Button session, settings, home, study;
    private DrawerLayout layout;

    FragmentClick clickInterface;
    DBHelper dbHelper = DBHelper.getInstance();
    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance() {
        return new MenuFragment();
    }
    public void setClickInterface(FragmentClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        session = (Button) view.findViewById(R.id.sessionsMenu);
        settings = (Button) view.findViewById(R.id.settingsMenu);
        home = (Button) view.findViewById(R.id.homeMenu);
        study = (Button) view.findViewById(R.id.studyMenu);
        textViewLogout = (TextView) view.findViewById(R.id.logout);

        session.setOnClickListener(v -> {
            if(getActivity() instanceof SessionActivity){
                clickInterface.buttonClicked("");
            }else{
                clickInterface.buttonClicked("");
                Intent intent = new Intent(getActivity().getApplicationContext(), SessionActivity.class);
                startActivity(intent);
            }
        });

        study.setOnClickListener(v -> {
            if(getActivity() instanceof TimerActivity){
                clickInterface.buttonClicked("");
            }else{
                clickInterface.buttonClicked("");
                Intent intent = new Intent(getActivity().getApplicationContext(), TimerActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(v -> {
            if(getActivity() instanceof SettingsActivity){
                clickInterface.buttonClicked("");
            }else{
                clickInterface.buttonClicked("");
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(v -> {
            if(getActivity() instanceof HomeActivity){
                clickInterface.buttonClicked("");
            }else {
                clickInterface.buttonClicked("");
                Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        textViewLogout.setOnClickListener(v -> {
            dbHelper.logout();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }
}