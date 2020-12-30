package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.UserInfo;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, rePassword, email;

    Button signIn;
    TextView login;

    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.re_password);
        email = (EditText) findViewById(R.id.email);

        signIn = (Button) findViewById(R.id.btnsignin);
        login = (TextView) findViewById(R.id.btnpreLogin);


        if(dbHelper.checkIfLogged()){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        signIn.setOnClickListener(v -> {
            String userName = username.getText().toString().trim();
            String userPass = password.getText().toString().trim();
            String userRePass = rePassword.getText().toString().trim();
            String userEmail = email.getText().toString().trim();

            if (userName.isEmpty())
                username.setError(getResources().getString(R.string.username_empty));
            else if(userPass.isEmpty())
                password.setError(getResources().getString(R.string.password_empty));
            else if(userRePass.isEmpty())
                rePassword.setError(getResources().getString(R.string.password_not_confirmed));
            else if(userEmail.isEmpty())
                email.setError(getResources().getString(R.string.email_empty));
            else {
                if(userPass.length() >= 6) {
                    if(userPass.equals(userRePass)) {
                        dbHelper.checkUsernameExists(userName, result -> {
                            if(result)
                                username.setError(getResources().getString(R.string.username_exists));
                            else
                                dbHelper.createUser(new UserInfo(userName, userPass, userEmail, " "), RegisterActivity.this, getApplicationContext());
                        });
                    } else
                        rePassword.setError(getResources().getString(R.string.password_conf_different));
                } else
                    password.setError(getResources().getString(R.string.password_too_small));
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        });
    }
}