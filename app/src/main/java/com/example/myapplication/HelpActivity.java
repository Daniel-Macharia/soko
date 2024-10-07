package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon;

    private EditText emailSubject, emailBody;
    private Button sendEmail;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.help_activity );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        emailSubject = findViewById( R.id.emailSubject );
        emailBody = findViewById( R.id.emailBody );
        sendEmail = findViewById( R.id.sendEmail );

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_subject = "", email_body = "";

                email_subject = emailSubject.getText().toString();
                email_body = emailBody.getText().toString();

                sendEmailToDevelopers(email_subject, email_body);
            }
        });

        homeIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                finish();
            }
        });

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( HelpActivity.this, LoginActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });
    }

    private void sendEmailToDevelopers(String emailSubject, String emailBody)
    {
        try {
            if( !UtilityClass.isNetworkConnectionAvailable(getApplicationContext(), new Handler(Looper.getMainLooper())) )
            {
                Toast.makeText(getApplicationContext(), "No internet connection." +
                        "\nPlease check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }

            Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
            selectorIntent.setData( Uri.parse("mailto:") );

            String recipientEmail = "ndungudanny444@gmail.com";

            final Intent emailIntent = new Intent( Intent.ACTION_SEND );
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
            emailIntent.setSelector( selectorIntent );

            startActivity( Intent.createChooser(emailIntent, "Send Email"));

            clearInput();

        }catch( Exception e )
        {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInput()
    {
        emailSubject.setText("");
        emailBody.setText("");
    }
}
