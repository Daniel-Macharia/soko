package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ProfileActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon, profilePicture;
    private TextView screenTitle, userEmail, userName, userPhone;


    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if( result.getResultCode() == Activity.RESULT_OK )
                {
                    Intent data = result.getData();

                    if( data != null && data.getData() != null )
                    {
                        Uri selectedImageUri = data.getData();

                        Bitmap selectedImageBitmap;

                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri
                            );

                            profilePicture.setImageBitmap(selectedImageBitmap);

                            LocalDatabase db = new LocalDatabase(getApplicationContext());
                            db.open();
                            db.setProfilePicture( selectedImageUri.toString() );
                            db.close();


                        }catch ( Exception e )
                        {
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.profile_activity );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        screenTitle = findViewById( R.id.screenTitle );
        screenTitle.setText("User Profile");

        profilePicture = findViewById( R.id.profilePicture );

        userName = findViewById( R.id.user_name );
        userEmail = findViewById( R.id.user_email );
        userPhone = findViewById( R.id.user_phone );

        setProfile();

        Intent intent = getIntent();
        String isFromShop = intent.getStringExtra("isFromShop");

        if( isFromShop != null && isFromShop.equals("shop") )
        {
            homeIcon.setOnClickListener( new View.OnClickListener(){

                @Override
                public void onClick( View view )
                {
                    Intent intent = new Intent( ProfileActivity.this, MyShopActivity.class );
                    startActivity(intent);

                    finishAffinity();
                }
            });
        }
        else {
            homeIcon.setOnClickListener( new View.OnClickListener(){

                @Override
                public void onClick( View view )
                {
                    Intent intent = new Intent( ProfileActivity.this, HomeActivity.class );
                    startActivity(intent);

                    finishAffinity();
                }
            });
        }

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( ProfileActivity.this, LoginActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    private void setProfile()
    {
        Intent intent = getIntent();
        //profilePicture.setImageBitmap(pic);
        userName.setText(intent.getStringExtra("userName"));//details[0]);
        userPhone.setText(intent.getStringExtra("userPhone"));//details[1]);
        userEmail.setText(intent.getStringExtra("userEmail"));//details[2]);
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        launcher.launch( intent );
    }

}
