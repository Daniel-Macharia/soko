package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class ResetPassword extends AppCompatActivity {

    private EditText password, confirm;
    private Button resetPassword;

    @Override
    public void onCreate( Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.reset_password );

        password = findViewById( R.id.password );
        confirm = findViewById( R.id.confirmPassword );
        resetPassword = findViewById( R.id.resetPassword );

        String userEmail = getIntent().getStringExtra("userMail");

        resetPassword.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String pass = password.getText().toString();
                String conf = confirm.getText().toString();

                if( validPassword(pass) )
                {
                    if(pass.equals(conf))
                    {
                        setUserPassword( userEmail, pass);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "The password and the confirm password do not match!", Toast.LENGTH_SHORT).show();
                        //password.setText("");
                        //confirm.setText("");
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Password too weak!" +
                            "\nIt should be at least 8 characters long and contain both upper and lowercase letters," +
                            "\n numbers, and a special character.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setUserPassword(String email, String password)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("user");


        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for( DataSnapshot user : task.getResult().getChildren() )
                {
                    String userMail = "";

                    if( user.hasChild("userEmail") )
                    {
                        userMail = user.child("userEmail").getValue().toString();
                        if( email.equals(userMail) )
                        {
                            //if( user.hasChild("userPassword") )
                            {
                                user.child("userPassword").getRef().setValue(password);
                                Toast.makeText(getApplicationContext(), "Your password was successfully reset.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                                startActivity(intent);
                                finishAffinity();

                                return;
                            }
                        }
                    }
                }

                Toast.makeText(getApplicationContext(), "Could not find user with specified email: " + email, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validPassword(String password)
    {
        if( UtilityClass.isPasswordStrong(password) )
        {
            return true;
        }

        return false;
    }
}
