package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon;
    private TextView screenTitle;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.profile_activity );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        screenTitle = findViewById( R.id.screenTitle );
        screenTitle.setText("User Profile");

        homeIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( ProfileActivity.this, HomeActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( ProfileActivity.this, MainActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });


    }
}
