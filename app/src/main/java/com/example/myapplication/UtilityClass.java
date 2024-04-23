package com.example.myapplication;

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

        if( (!password.equals("")) && (password.length() >= 8) && password.matches(lowerAlphaRegex)
                && password.matches(upperAlphaRegex) && password.matches(integerRegex) && password.matches(specialCharIndex) )
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
}
