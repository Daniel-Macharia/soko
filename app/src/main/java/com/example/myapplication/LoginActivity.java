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
            }
        });
    }

    private void checkLoginCredentials( String userEmail, String userPass)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("buyer");

        verifyUser( ref, userEmail, userPass, true);
    }

    private boolean verifyUser( DatabaseReference ref, String userEmail, String userPassword, boolean moreUsers)
    {
        try {
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String email = "", pass = "";
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

                        if( userEmail.equals( email ) )
                        {
                            if(userPassword.equals( pass ))
                            {
                                Intent intent = new Intent( LoginActivity.this, moreUsers ? HomeActivity.class : MyShopActivity.class );
                                intent.putExtra("userName", new String( child.child("userName").getValue().toString()));
                                intent.putExtra("userEmail", new String( child.child("userEmail").getValue().toString()));
                                intent.putExtra("userPhone", new String( child.child("userPhone").getValue().toString()));
                                intent.putExtra("userLocation", new String( child.child("userLocation").getValue().toString()));

                                startActivity(intent);
                                finishAffinity();

                                return;
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

                    if( moreUsers )
                    {
                        DatabaseReference buyerRef = ref.getDatabase().getReference("seller");
                        verifyUser( buyerRef, new String(userEmail), new String(userPassword), false);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No user with specified email exists!" +
                                "\nPlease sign up", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}