package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private Button create, login;
    private EditText username, email, password, confirmPassword;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sign_up );

        username = findViewById( R.id.signup_username );
        email = findViewById( R.id.signup_email );
        password = findViewById( R.id.signup_password );
        confirmPassword = findViewById( R.id.signup_confirm );

        login = findViewById( R.id.signup_login );
        create = findViewById( R.id.signup_create );

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();

                String message = "username: " + user
                        + "\nEmail: " + mail
                        + "\nPassword: " + pass
                        + "\nConfirm: " + confirm;

                Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SignUp.this, MainActivity.class );
                startActivity( intent );
            }
        });
    }
}
