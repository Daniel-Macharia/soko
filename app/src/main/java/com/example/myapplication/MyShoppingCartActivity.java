package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyShoppingCartActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon;

    private TextView screenTitle;

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_shopping_cart );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        screenTitle = findViewById( R.id.screenTitle );
        screenTitle.setText("My Items");

        homeIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( MyShoppingCartActivity.this, HomeActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( MyShoppingCartActivity.this, MainActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

    }
}
