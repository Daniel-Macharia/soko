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
        shopNameContainer.removeAllViews();

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopNameContainer.addView(shopNameLabel);
                shopNameContainer.addView(shopNameInput);
            }
        });

        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopNameContainer.removeAllViews();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String user, pass, confirm, mail, phoneNumber, loc, userType, shopName;

                    userType = ( seller.isChecked() ? "seller" : ( buyer.isChecked() ? "buyer" : null));

                    shopName = ( seller.isChecked() ? shopNameInput.getText().toString() : null);

                    user = username.getText().toString();
                    pass = password.getText().toString();
                    confirm = confirmPassword.getText().toString();
                    mail = email.getText().toString();
                    phoneNumber = phone.getText().toString();
                    loc = location.getText().toString();

                    if(validateInput(user, phoneNumber, pass, confirm, mail, userType, shopName))
                    {
                        addUser( user, pass, phoneNumber, mail, loc, userType, shopName);
                    }
                }catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUser( String userName, String userPassword, String userPhone, String userMail, String userLocation, String userType, String shopName)
    {
        if(userType.equals("seller"))
        {
            Seller user = new Seller(userName, userPassword, userPhone, userMail, userLocation, shopName);

            if( UtilityClass.isNetworkConnectionAvailable( getApplicationContext(), new Handler(Looper.getMainLooper()) ) )
            {
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                DatabaseReference ref = db.getReference("seller");

                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        try {
                            String userMail = "", sellerPhone;
                            for( DataSnapshot seller : task.getResult().getChildren() )
                            {
                                if( seller.hasChild("userEmail") )
                                {
                                    userMail = new String(seller.child("userEmail").getValue().toString());
                                }

                                if( user.userEmail.equals(userMail) )
                                {
                                    Toast.makeText(getApplicationContext(), "A user with this email exists!\nPlease log in", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            ref.push().setValue(user);
                            Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
                            launchLogin( new String( user.userEmail ) );
                        }catch(Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {
                Toast.makeText(getApplicationContext(), "No internet connection." +
                        "\nPlease check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Buyer user = new Buyer( userName, userPassword, userPhone, userMail, userLocation);

            if( UtilityClass.isNetworkConnectionAvailable( getApplicationContext(), new Handler(Looper.getMainLooper()) ) )
            {
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                DatabaseReference ref = db.getReference("buyer");

                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        try
                        {
                            String userMail = "", buyerPhone;
                            for( DataSnapshot buyer : task.getResult().getChildren() )
                            {
                                if( buyer.hasChild("userEmail") )
                                {
                                    userMail = new String(buyer.child("userEmail").getValue().toString());
                                }

                                if( user.userEmail.equals(userMail) )
                                {
                                    Toast.makeText(getApplicationContext(), "A user with this email exists!\nPlease log in", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            ref.push().setValue(user);
                            Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
                            launchLogin( new String( user.userEmail ) );
                        }catch(Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No internet connection." +
                        "\nPlease check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validateInput(String username, String phoneNumber, String password, String confirmPassword, String email, String userType, String shopName)
    {
        if( username.equals("") )
        {
            Toast.makeText(getApplicationContext(), "Username cannot be blank\n", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!UtilityClass.isNameValid( username ))
        {
            Toast.makeText(getApplicationContext(), "Invalid username!" +
                    "\n it may only contain characters and digits", Toast.LENGTH_SHORT).show();
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

        if( shopName != null && !UtilityClass.isValidShopName(shopName) )
        {
            Toast.makeText(getApplicationContext(), "Invalid shop name!" +
                    "\nA shop name may only contain characters and numbers " +
                    "\nhaving a maximum of 30 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createNewUserAccount( Buyer user )
    {
        /*LocalDatabase db = new LocalDatabase( getApplicationContext() );
        db.open();
        long id = db.addNewUser( user.userName, user.userPhone, user.userName, user.userEmail);
        db.close();

        if( id != -1 )
        {
            Toast.makeText(getApplicationContext(), "Account Created Successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        }
        */
    }

    private void clearFields()
    {
        username.setText("");
        phone.setText("");
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
        shopNameInput.setText("");
        seller.setChecked(false);
        buyer.setChecked(false);
    }

    private void launchLogin(String userMail)
    {
        Intent intent = new Intent( SignUp.this, LoginActivity.class );
        intent.putExtra("userMail", userMail);
        startActivity( intent );
        finish();
    }
}


class Buyer
{
    public String userName, userPhone, userEmail, userLocation, userPassword;
    public Buyer( String userName, String userPassword
            , String userPhone, String userEmail
            , String userLocation)
    {
        this.userName = new String(userName);
        this.userPhone = new String(userPhone);
        this.userEmail = new String(userEmail);
        this.userLocation = new String(userLocation);
        this.userPassword = new String(userPassword);
    }
}

class Seller extends Buyer
{
    public String shopName;

    public Seller( String userName, String userPassword
            , String userPhone, String userEmail
            , String userLocation, String shopName)
    {
        super( userName, userPassword, userPhone, userEmail, userLocation);
        this.shopName = new String( shopName );
    }

}