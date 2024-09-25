package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private Button seller, buyer;
    private EditText username, email, password, confirmPassword, phone, location;


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
        location = findViewById( R.id.signup_user_location);

        seller = findViewById( R.id.seller );
        buyer = findViewById( R.id.buyer );

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail, phoneNumber, loc;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();
                phoneNumber = phone.getText().toString();
                loc = location.getText().toString();

                if(validateInput(user, phoneNumber, pass, confirm, mail, "seller"))
                {
                    addUser( user, phoneNumber, mail, loc, true);
                    //createNewUserAccount( user, phoneNumber, pass, mail, "seller");
                    //launchLogin();
                }
            }
        });

        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail, phoneNumber, loc;

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();
                phoneNumber = phone.getText().toString();
                loc = location.getText().toString();

                if(validateInput(user, phoneNumber, pass, confirm, mail, "buyer"))
                {
                    addUser( user, phoneNumber, mail, loc, false);
                    //createNewUserAccount( user, phoneNumber, pass, mail, "buyer");
                    //launchLogin();
                }
            }
        });
    }

    private void addUser( String userName, String userPhone, String userMail, String userLocation, boolean userIsSeller)
    {
        User user = new User(userName, userPhone, userMail, userLocation, userIsSeller);

        if( UtilityClass.isNetworkConnectionAvailable(  getApplicationContext(), new Handler(Looper.getMainLooper()) ))
        {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("user");

            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    boolean userAvailable = false;
                    for( DataSnapshot child : task.getResult().getChildren() )
                    {
                        if( child.hasChild("userPhone") )
                        {
                            String phone = child.child("userPhone").getValue().toString();
                            if( user.userPhone.equals(phone) )
                            {
                                userAvailable = true;
                                break;
                            }
                        }
                    }

                    if( !userAvailable )
                    {
                        ref.push().setValue(user);
                        //add user to local database
                        createNewUserAccount(user);
                        Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //perform user account recovery
                        Toast.makeText(getApplicationContext(), "A user with this phone exists!\nEnter details to login.", Toast.LENGTH_SHORT).show();
                    }

                    launchLogin();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet connection!" +
                    "\nPlease connect to the internet and try again.", Toast.LENGTH_SHORT).show();
        }
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

        return true;
    }

    private void createNewUserAccount( User user )
    {
        LocalDatabase db = new LocalDatabase( getApplicationContext() );
        db.open();
        long id = db.addNewUser( user.userName, user.userPhone, user.userName, user.userEmail, user.userIsSeller);
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


class User
{
    public String userName, userPhone, userEmail, userLocation;
    public boolean userIsSeller;
    public User( String userName, String userPhone
            , String userEmail, String userLocation
            , boolean userIsSeller)
    {
        this.userName = new String(userName);
        this.userPhone = new String(userPhone);
        this.userEmail = new String(userEmail);
        this.userLocation = new String(userLocation);
        this.userIsSeller = userIsSeller;
    }
}