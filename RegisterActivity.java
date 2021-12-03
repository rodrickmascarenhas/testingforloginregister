package com.example.testingforregister_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements ObservationView{
    EditText editTextUsername, editTextEmail, editTextPassword, editTextCnfPassword;
    Button buttonRegister;
    TextView textViewLogin;
    private UserDAO userDao;
    Service service;
    String errormessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCnfPassword = findViewById(R.id.editTextCnfPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        userDao = Room.databaseBuilder(this, UserDatabase.class, "mi-database.db")
                .allowMainThreadQueries()
                .build().getUserDao();

        service = new Service(this,userDao);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.OnClick();
                String userName = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String passwordConf = editTextCnfPassword.getText().toString().trim();

                if (password.equals(passwordConf)) {
                    User user = new User(userName,password,email);
                    userDao.insert(user);
                    Intent moveToLogin = new Intent
                            (RegisterActivity.this, MainActivity.class);
                    startActivity(moveToLogin);

                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Password is not matching", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public String getUsername() {
        return editTextUsername.getText().toString();
    }

    @Override
    public String getEmail() {
        return editTextEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return editTextPassword.getText().toString();
    }

    @Override
    public String getPasswordConf() {
        return editTextCnfPassword.getText().toString();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public String showNullError() {
        errormessage = "No input provided";
        return errormessage;
    }

    @Override
    public String showEmptyStringError() {
        errormessage = "User provided whitespace values";
        return errormessage;
    }

    @Override
    public String showPassNotMatch() {
        errormessage = "Password is not matching";
        return errormessage;
    }

    @Override
    public String showRegisteredSuccess() {
        errormessage = "User Registration Success";
        return errormessage;
    }

    @Override
    public String showNotRegistered() {
        errormessage = "Database is null reference";
        return errormessage;
    }

    @Override
    public String showNoRecordFound() {
        errormessage = "Database is not empty";
        return errormessage;
    }
}