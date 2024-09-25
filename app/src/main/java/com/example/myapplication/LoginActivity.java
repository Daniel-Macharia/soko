package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private Button login;
    private EditText userEmail, password;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userEmail = findViewById( R.id.login_userEmail );
        password = findViewById( R.id.login_password );

        login = findViewById( R.id.login );
        //signUp = findViewById( R.id.signup );
        forgotPassword = findViewById( R.id.forgot_password );

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Reset password!", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone, pass;

                phone = userEmail.getText().toString();
                pass = password.getText().toString();

                if( UtilityClass.isNetworkConnectionAvailable(getApplicationContext(), new Handler(Looper.getMainLooper())))
                {
                    checkLoginCredentials(phone, pass);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No internet connection!" +
                            "\nPlease check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }

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

                    if( userEmail.equals( email ) && userPass.equals( pass ) )
                    {
                        //valid user credentials. allow login
                        isValid = true;
                        break;
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

                    startActivity(intent);
                    finishAffinity();
                }
                else {
                    //invalid credentials. Alert user
                    Toast.makeText(getApplicationContext(), "Invalid email or password!" +
                            "\nPlease confirm and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}