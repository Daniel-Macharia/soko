package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private Button seller, buyer;
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

        seller = findViewById( R.id.seller );
        buyer = findViewById( R.id.buyer );

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();

                validateInput(user, pass, confirm, mail);

                String message = "username: " + user
                        + "\nEmail: " + mail
                        + "\nPassword: " + pass
                        + "\nConfirm: " + confirm;

                Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();

                validateInput(user, pass, confirm, mail);

            }
        });
    }

    private void validateInput(String username, String password, String confirmPassword, String email)
    {
        if( !UtilityClass.isNameValid( username ) )
        {
            Toast.makeText(getApplicationContext(), "Username cannot be blank\n" +
                    "It may only contain characters and digits", Toast.LENGTH_SHORT).show();
            return;
        }

        if( !UtilityClass.isEmailValid( email ) )
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if( password.equals( confirmPassword ) )
        {
            if( ! UtilityClass.isPasswordStrong( password ) )
            {
                Toast.makeText(getApplicationContext(), "Password is too weak!" +
                        "\n It should contain both upper and lower case letters " +
                        "\nand at least one special character", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password and the confirm password do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        createNewUserAccount( username, password, email);
    }

    private void createNewUserAccount( String username, String password, String email)
    {
        LocalDatabase db = new LocalDatabase( getApplicationContext() );
        db.open();
        long id = db.addNewUser( username, password, email);
        db.close();

        if( id != -1 )
        {
            Toast.makeText(getApplicationContext(), "Account Created Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
