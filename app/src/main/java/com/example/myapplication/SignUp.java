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
    private EditText username, email, password, confirmPassword, phone;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sign_up );

        username = findViewById( R.id.signup_username );
        email = findViewById( R.id.signup_email );
        password = findViewById( R.id.signup_password );
        confirmPassword = findViewById( R.id.signup_confirm );
        phone = findViewById( R.id.signup_phone );

        seller = findViewById( R.id.seller );
        buyer = findViewById( R.id.buyer );

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail, phoneNumber;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();
                phoneNumber = phone.getText().toString();

                if(validateInput(user, phoneNumber, pass, confirm, mail, "seller"))
                {
                    launchLogin();
                }
            }
        });

        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail, phoneNumber;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();
                phoneNumber = phone.getText().toString();

                if(validateInput(user, phoneNumber, pass, confirm, mail, "buyer"))
                {
                    launchLogin();
                }
            }
        });
    }

    private boolean validateInput(String username, String phoneNumber, String password, String confirmPassword, String email, String userType)
    {
        if( !UtilityClass.isNameValid( username ) )
        {
            Toast.makeText(getApplicationContext(), "Username cannot be blank\n" +
                    "It may only contain characters and digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( !UtilityClass.isEmailValid( email ) )
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( password.equals( confirmPassword ) )
        {
            if( ! UtilityClass.isPasswordStrong( password ) )
            {
                Toast.makeText(getApplicationContext(), "Password is too weak!" +
                        "\n It should contain both upper and lower case letters " +
                        "\nand at least one special character", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password and the confirm password do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }

        createNewUserAccount( username, phoneNumber, password, email, userType);
        return true;
    }

    private void createNewUserAccount( String username, String phoneNumber, String password, String email, String userType)
    {
        LocalDatabase db = new LocalDatabase( getApplicationContext() );
        db.open();
        long id = db.addNewUser( username, phoneNumber, password, email, userType);
        db.close();

        if( id != -1 )
        {
            Toast.makeText(getApplicationContext(), "Account Created Successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        }

    }

    private void clearFields()
    {
        username.setText("");
        phone.setText("");
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    private void launchLogin()
    {
        Intent intent = new Intent( SignUp.this, LoginActivity.class );
        startActivity( intent );
        finish();
    }
}
