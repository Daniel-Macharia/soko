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

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;

public class SendOTP extends AppCompatActivity {
    private Button resendOTP, confirmOTP;
    private EditText enteredOTP;

    private String otp;

    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.send_otp );

        resendOTP = findViewById( R.id.resend );
        confirmOTP = findViewById( R.id.confirm );
        enteredOTP = findViewById( R.id.entered_otp );

        sendOTP();

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP();
            }
        });

        confirmOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered = enteredOTP.getText().toString();

                if( otp.equals(entered) )
                {
                    //correct OTP, allow to reset password
                    Intent intent = new Intent( SendOTP.this, ResetPassword.class );
                    intent.putExtra("userMail", getUserEmail());
                    startActivity(intent);
                }
                else
                {
                    //wrong OTP ask to resend OTP
                    Toast.makeText(getApplicationContext(), "Incorrect OTP entered!" +
                            "\nClick resend to receive a new one.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOTP()
    {
        otp = String.valueOf( (1000 + (int)( Math.random() * 10000 ) ) );

        //logic to send otp via e-mail
        if( UtilityClass.isNetworkConnectionAvailable( getApplication(), new Handler(Looper.getMainLooper())) )
        {
            String userEmail = getUserEmail();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String senderEmail = "sanaautoto@gmail.com";
                        String senderEmailPassword = "vues ziws onvl qldq";
                        String host = "smtp.gmail.com";

                        Properties properties = System.getProperties();
                        properties.put("mail.smtp.host", host);
                        properties.put("mail.smtp.port", "465");
                        //properties.put("mail.smtp.port", "587");
                        properties.put("mail.smtp.ssl.enable", true);
                        properties.put("mail.smtp.auth", true);

                        Session session = Session.getInstance( properties, new Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(senderEmail, senderEmailPassword );
                            }
                        });

                        MimeMessage mimeMessage = new MimeMessage(session);
                        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail) );
                        mimeMessage.setSubject("Subject: SOKO password recovery");
                        mimeMessage.setText("Your SOKO one time pin is: " + otp);

                        Transport.send(mimeMessage);
                        toastForLonger("A one time pin has been sent to your email");
                    }catch(Exception e)
                    {
                        toast("Error: " + e);
                    }
                }
            }).start();
        }
    }

    private void toast(String message)
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toastForLonger(String message)
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private String getUserEmail()
    {
        return getIntent().getStringExtra("userMail");
    }
}
