package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private RadioButton seller, buyer;

    private Button signUp;

    private LinearLayout shopNameContainer;
    private EditText username, email, password, confirmPassword, phone, location;

    private TextView shopNameLabel;
    private EditText shopNameInput;

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

        shopNameLabel = findViewById(R.id.shop_name_label);

        shopNameInput = findViewById(R.id.shop_name_input);

        shopNameContainer = findViewById( R.id.shop_name_container );

        signUp = findViewById( R.id.sign_up );

        //make the shop name input field appear only when seller is selected
        //shopNameLabel.setMaxHeight(0);
        //shopNameInput.setMaxHeight(0);
        shopNameContainer.removeAllViews();

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUp.this, "selected seller!", Toast.LENGTH_SHORT).show();
                /*TextView label = new TextView(SignUp.this);
                label.setText("Shop name:");
                label.setGravity(Gravity.END|Gravity.CENTER);
                label.setTextColor(Color.BLACK);

                EditText input = new EditText(SignUp.this);
                input.setHint("enter shop name");
                input.setGravity(Gravity.START|Gravity.CENTER);
                input.setTextColor( getResources().getColor(R.color.blue) );

                label.setId(R.id.shop_name_label);
                input.setId(R.id.shop_name_input);

                shopNameContainer.addView(label);
                shopNameContainer.addView(input); */

                //shopNameLabel.setMaxHeight(32);
                //shopNameInput.setMaxHeight(32);
                shopNameContainer.addView(shopNameLabel);
                shopNameContainer.addView(shopNameInput);
            }
        });

        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUp.this, "selected buyer!", Toast.LENGTH_SHORT).show();
                //shopNameLabel.setMaxHeight(0);
                //shopNameInput.setMaxHeight(0);
                shopNameContainer.removeAllViews();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass, confirm, mail, phoneNumber, loc, userType;

                userType = ( seller.isChecked() ? "seller" : ( buyer.isChecked() ? "buyer" : null));

                user = username.getText().toString();
                pass = password.getText().toString();
                confirm = confirmPassword.getText().toString();
                mail = email.getText().toString();
                phoneNumber = phone.getText().toString();
                loc = location.getText().toString();

                if(validateInput(user, phoneNumber, pass, confirm, mail, userType))
                {
                    addUser( user, pass, phoneNumber, mail, loc, true);
                    //createNewUserAccount( user, phoneNumber, pass, mail, "seller");
                    //launchLogin();
                }
            }
        });
    }

    private void addUser( String userName, String userPassword, String userPhone, String userMail, String userLocation, boolean userIsSeller)
    {
        User user = new User(userName, userPassword, userPhone, userMail, userLocation, userIsSeller);

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
                        if( child.hasChild("userEmail") )
                        {
                            String email = child.child("userEmail").getValue().toString();
                            if( user.userEmail.equals(email) )
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
                        Toast.makeText(getApplicationContext(), "A user with this email exists!" +
                                "\nEnter details to login.", Toast.LENGTH_SHORT).show();

                    }

                    launchLogin(userMail);
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

        if( userType == null )
        {
            Toast.makeText(getApplicationContext(), "Please select a 'sign up' option.", Toast.LENGTH_SHORT).show();
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

    private void launchLogin(String userMail)
    {
        Intent intent = new Intent( SignUp.this, LoginActivity.class );
        intent.putExtra("userMail", userMail);
        startActivity( intent );
        finish();
    }
}


class User
{
    public String userName, userPhone, userEmail, userLocation, userPassword;
    public boolean userIsSeller;
    public User( String userName, String userPassword
            , String userPhone, String userEmail
            , String userLocation, boolean userIsSeller)
    {
        this.userName = new String(userName);
        this.userPhone = new String(userPhone);
        this.userEmail = new String(userEmail);
        this.userLocation = new String(userLocation);
        this.userPassword = new String(userPassword);
        this.userIsSeller = userIsSeller;
    }
}