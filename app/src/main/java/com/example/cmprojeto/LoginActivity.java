package com.example.cmprojeto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cmprojeto.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView signIn, resetPassword;
    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.btnLogin);
        signIn = (TextView) findViewById(R.id.registerLabel);
        resetPassword = (TextView) findViewById(R.id.passwordReset);

        if(dbHelper.checkIfLogged()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        login.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if(userEmail.isEmpty() || userPassword.isEmpty())
                //Toast.makeText(LoginActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                if(userEmail.isEmpty()){
                    email.setError("Email can't be empty");
                }else
                    password.setError("Password can't be empty");
            else{
                dbHelper.loginUser(userEmail, userPassword, LoginActivity.this, getApplicationContext());
            }
        });

        signIn.setOnClickListener(v -> {
            Intent intent  = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        resetPassword.setOnClickListener(v -> {
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle(getResources().getString(R.string.reset_password));
            passwordResetDialog.setMessage(getResources().getString(R.string.pls_enter_email));
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                String email = resetMail.getText().toString();
                dbHelper.resetPassword(email, LoginActivity.this);
            });

            passwordResetDialog.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {

            });

            passwordResetDialog.create().show();
        });
    }
}