package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    EditText username, password, rePassword, email;

    Button signIn;
    TextView login;

    DBHelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dBhelper = new DBHelper();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.re_password);
        email = (EditText) findViewById(R.id.email);

        signIn = (Button) findViewById(R.id.btnsignin);
        login = (TextView) findViewById(R.id.btnpreLogin);


        if(dBhelper.checkIfLogged()){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString().trim();
                String userPass = password.getText().toString().trim();
                String userRePass = rePassword.getText().toString().trim();
                String userEmail = email.getText().toString().trim();

                if (userName.isEmpty() || userPass.isEmpty() || userRePass.isEmpty()|| userEmail.isEmpty()){
                    if (userName.isEmpty()){
                        username.setError("Username can't be empty");
                    }else if(userPass.isEmpty()){
                        password.setError("Password can't be empty");
                    }else if(userRePass.isEmpty()){
                        rePassword.setError("Please confirm password");
                    }else {
                        email.setError("Email can't be empty");
                    }
                }

                else{
                    if(userPass.length() >= 6){
                        if(userPass.equals(userRePass)){
                            dBhelper.checkUsernameExists(userName,userEmail,userPass, username, SignInActivity.this, getApplicationContext(), true);
                        }else{
                            //Toast.makeText(SignInActivity.this, "Password and password confirmation are different", Toast.LENGTH_SHORT).show();
                            rePassword.setError("Password and confirmation are different");
                        }
                    }else {
                        password.setError("Password needs to have at least 6 characters");
                    }

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}