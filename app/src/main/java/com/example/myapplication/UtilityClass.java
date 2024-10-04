package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class UtilityClass {

    public static boolean isNameValid(String name)
    {
        String nameRegex = "^[a-zA-Z0-9]{1,60}$";

        return (!name.equals("")) && name.matches( nameRegex );
    }

    public static boolean isPasswordStrong( String password )
    {
        String lowerAlphaRegex = "^[\\w\\W]{0,60}[a-z]{1,20}[\\w\\W]{0,60}$";
        String upperAlphaRegex = "^[\\w\\W]{0,60}[A-Z]{1,20}[\\w\\W]{0,60}$";
        String integerRegex = "^[\\w\\W]{0,60}[0-9]{1,20}[\\w\\W]{0,60}$";
        String specialCharIndex = "^[\\w\\W]{0,60}[`~!@#$%^&*()]{1,20}[\\w\\W]{0,60}$";

        if( (!password.equals("")) && (password.length() >= 8)
                && password.matches(lowerAlphaRegex) && password.matches(upperAlphaRegex)
                && password.matches(integerRegex) && password.matches(specialCharIndex) )
        {
            return true;
        }

        return false;
    }

    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-z0-9-]{1,100}@[a-z]{1,10}[.]{0,1}[a-z]{1,20}[.]{0,1}[a-z]{1,10}[.]{0,1}[a-z]{1,10}$";

        return (!email.equals("")) && email.matches( emailRegex );
    }

    public static boolean isValidShopName( String shopName )
    {
        if( shopName == null )
        {
            return false;
        }

        String shopNameRegex = "^[a-zA-Z0-9]{1,30}$";

        if( !shopName.matches( shopNameRegex ) )
        {
            return false;
        }

        return true;
    }

    public static byte[] convertBitmapToByteArray( Bitmap image )
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);

        return stream.toByteArray();
    }

    public static Bitmap convertByteArrayToBitmap( byte[] imageData )
    {
        Bitmap image = BitmapFactory.decodeByteArray( imageData, 0, imageData.length );

        return image;
    }

    public static boolean isConnectedToInternet(Context context, Handler handler)
    {
        boolean isConnected = true;
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = cm.getActiveNetworkInfo();

            if(!( info != null && info.isAvailable() ))
            {
                isConnected = false;
            }

        }catch(Exception e)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return isConnected;
    }

    public static boolean isNetworkConnectionAvailable( Context context, Handler handler )
    {
        boolean isAvailable = false;

        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Network network = cm.getActiveNetwork();

            NetworkCapabilities netCaps = cm.getNetworkCapabilities( network );

            if( netCaps.hasCapability( NetworkCapabilities.NET_CAPABILITY_VALIDATED) )
            {
                isAvailable = true;
            }
        }catch(Exception e)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }

        return isAvailable;
    }
}
