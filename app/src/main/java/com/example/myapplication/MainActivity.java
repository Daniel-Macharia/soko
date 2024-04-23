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

        setSignUpButton();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass;

                user = username.getText().toString();
                pass = password.getText().toString();

                LocalDatabase db = new LocalDatabase( getApplicationContext() );
                db.open();
                if( db.userExists() )
                {
                    if( db.isLegitimateUser(user, pass) )
                    {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class );
                        startActivity(intent);

                        finishAffinity();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "User does not exist\nPlease sign up", Toast.LENGTH_SHORT).show();
                }

                db.close();

            }
        });
    }

    @Override
    public void onResume() {
        setSignUpButton();

        super.onResume();
    }

    private void setSignUpButton()
    {
        LocalDatabase db = new LocalDatabase( getApplicationContext() );
        db.open();
        if( db.userExists() )
        {
            signUp.setVisibility( View.INVISIBLE );
        }
        else
        {
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( MainActivity.this, SignUp.class );
                    startActivity(intent);
                }
            });
        }

        db.close();
    }
}