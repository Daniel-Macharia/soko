package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);

        boolean exists = false;
        LocalDatabase db = new LocalDatabase(getApplicationContext());
        db.open();
        exists = db.userExists();
        db.close();

        Intent intent;
        if( exists )
        {
            intent = new Intent( MainActivity.this, LoginActivity.class );
        }
        else
        {
            intent = new Intent( MainActivity.this, SignUp.class );
        }

        startActivity(intent);
        finish();
    }
}
