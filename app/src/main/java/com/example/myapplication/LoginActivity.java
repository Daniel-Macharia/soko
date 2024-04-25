package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText username, password;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username = findViewById( R.id.login_username );
        password = findViewById( R.id.login_password );

        login = findViewById( R.id.login );
        //signUp = findViewById( R.id.signup );
        forgotPassword = findViewById( R.id.forgot_password );

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Reset password!", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass;

                user = username.getText().toString();
                pass = password.getText().toString();

                LocalDatabase db = new LocalDatabase( getApplicationContext() );
                db.open();
                String type = db.getUserType();
                if( db.userExists() )
                {
                    if( db.isLegitimateUser(user, pass) )
                    {
                        Intent intent;
                        if( type.equals("seller") )
                        {
                            intent = new Intent(LoginActivity.this, MyShopActivity.class );
                        }
                        else
                        {
                            intent = new Intent(LoginActivity.this, HomeActivity.class );
                        }

                        startActivity(intent);
                        finishAffinity();
                    }
                }

                db.close();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}