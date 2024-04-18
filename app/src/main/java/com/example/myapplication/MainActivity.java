package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button login, signUp;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById( R.id.login_username );
        password = findViewById( R.id.login_password );

        login = findViewById( R.id.login );
        signUp = findViewById( R.id.signup );

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, SignUp.class );
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass;

                user = username.getText().toString();
                pass = password.getText().toString();

                String message = "Username: " + user
                        + "\nPassword: " + pass;

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class );
                startActivity(intent);
            }
        });
    }
}