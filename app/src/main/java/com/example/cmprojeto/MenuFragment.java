package com.example.cmprojeto;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Color;

public class MenuFragment extends Fragment {

    private ImageButton textViewLogout;
    private TextView usernameMenu;
    private Button session, settings, home, study;
    private ImageView avatarMenu;

    FragmentClick clickInterface;
    DBHelper dbHelper = DBHelper.getInstance();

    public MenuFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        session = (Button) view.findViewById(R.id.sessionsMenu);
        settings = (Button) view.findViewById(R.id.settingsMenu);
        home = (Button) view.findViewById(R.id.homeMenu);
        study = (Button) view.findViewById(R.id.studyMenu);
        textViewLogout = (ImageButton) view.findViewById(R.id.logout);
        avatarMenu = (ImageView) view.findViewById(R.id.avatarMenu);
        usernameMenu = (TextView) view.findViewById(R.id.usernameMenu);

        if(dbHelper.emailNotVerified()){
            session.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.reddish));
        }

        setupCallbacks();
        populateActivity();

        return view;
    }

    private void populateActivity() {
        if(!DBHelper.USER.isPopulated()) {
            dbHelper.getUserInfo(user -> {
                DBHelper.USER.populateUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getDescription(), user.getAvatar());
                fillControls(user.getUsername(), user.getAvatar());
            });
        } else {
            fillControls(DBHelper.USER.getUsername(), DBHelper.USER.getAvatar());
        }
    }

    private void fillControls(String username, byte[] avatar) {
        usernameMenu.setText(username);

        if(avatar != null)
            avatarMenu.setImageBitmap(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
    }

    private void setupCallbacks() {
        if(!dbHelper.emailNotVerified()){
            session.setOnClickListener(v -> {
                if(getActivity() instanceof SessionActivity){
                    clickInterface.menuClicked();
                } else {
                    clickInterface.menuClicked();
                    Intent intent = new Intent(getActivity().getApplicationContext(), SessionActivity.class);
                    startActivity(intent);
                }
            });
        }

        study.setOnClickListener(v -> {
            if(getActivity() instanceof TimerActivity){
                clickInterface.menuClicked();
            } else {
                clickInterface.menuClicked();
                Intent intent = new Intent(getActivity().getApplicationContext(), TimerActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(v -> {
            if(getActivity() instanceof SettingsActivity){
                clickInterface.menuClicked();
            } else {
                clickInterface.menuClicked();
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(v -> {
            if(getActivity() instanceof HomeActivity){
                clickInterface.menuClicked();
            } else {
                clickInterface.menuClicked();
                Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        textViewLogout.setOnClickListener(v -> {
            dbHelper.logout();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}