package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;

public class HelpActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.help_activity );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        homeIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( HelpActivity.this, HomeActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( HelpActivity.this, MainActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });
    }
}
