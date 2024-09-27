package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button login, signUp;
    private EditText userEmail, password;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userEmail = findViewById( R.id.login_userEmail );
        password = findViewById( R.id.login_password );

        login = findViewById( R.id.login );
        signUp = findViewById( R.id.sign_up );
        forgotPassword = findViewById( R.id.forgot_password );

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SendOTP.class);
                intent.putExtra("userMail", userEmail.getText().toString() );//getIntent().getStringExtra("userMail"));
                startActivity(intent);
            }
        });

        forgotPassword.setVisibility(View.INVISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;

                email = userEmail.getText().toString();
                pass = password.getText().toString();

                if( UtilityClass.isNetworkConnectionAvailable(getApplicationContext(), new Handler(Looper.getMainLooper())))
                {
                    checkLoginCredentials(email, pass);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No internet connection!" +
                            "\nPlease check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void checkLoginCredentials( String userEmail, String userPass)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("user");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String email = "", pass = "";
                User correctUser = new User("", "", "", "", "", false);
                boolean userIsSeller = false, isValid = false;
                for( DataSnapshot child : task.getResult().getChildren() )
                {
                    if( child.hasChild("userEmail") )
                    {
                        email = child.child("userEmail").getValue().toString();
                    }

                    if( child.hasChild("userPassword") )
                    {
                        pass = child.child("userPassword").getValue().toString();
                    }

                    if( child.hasChild("userIsSeller") )
                    {
                        userIsSeller = (boolean) child.child("userIsSeller").getValue();
                    }

                    if( userEmail.equals( email ) )
                    {
                        if(userPass.equals( pass ))
                        {
                            //valid user credentials. allow login
                            isValid = true;

                            correctUser.userName = new String( child.child("userName").getValue().toString());
                            correctUser.userPhone = new String( child.child("userPhone").getValue().toString());
                            correctUser.userEmail = new String( child.child("userEmail").getValue().toString());
                            correctUser.userLocation = new String( child.child("userLocation").getValue().toString());
                            correctUser.userIsSeller = (boolean) child.child("userIsSeller").getValue();
                            correctUser.userPassword = ""; // new String( child.child("userLocation").getValue().toString());

                            break;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Invalid password!" +
                                    "\nPlease confirm and try again", Toast.LENGTH_SHORT).show();
                            forgotPassword.setVisibility(View.VISIBLE);
                            password.setText("");
                            return;
                        }
                    }
                }

                if( isValid )
                {
                    Intent intent;
                    if( userIsSeller )
                    {
                        intent = new Intent(LoginActivity.this, MyShopActivity.class );
                    }
                    else
                    {
                        intent = new Intent(LoginActivity.this, HomeActivity.class );
                    }

                    intent.putExtra("userName", correctUser.userName);
                    intent.putExtra("userEmail", correctUser.userEmail);
                    intent.putExtra("userPhone", correctUser.userPhone);
                    intent.putExtra("userLocation", correctUser.userLocation);
                    intent.putExtra("userIsSeller", correctUser.userIsSeller);

                    startActivity(intent);
                    finishAffinity();
                }
                else {
                    //invalid credentials. Alert user
                    Toast.makeText(getApplicationContext(), "No user with the entered email exists!" +
                            "\nPlease sign up.", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent( LoginActivity.this, SignUp.class);
                    startActivity(intent);
                    finishAffinity();*/
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}