package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class LocalDatabase {

    private static final String username = "username";
    private static final String userPassword = "user_password";
    private static final String userEmail = "user_email";

    private static final String tableName = "user_table";

    private static final String dbName = "localDatabase";
    private static final int dbVersion = 1;

    private Context context;
    private DBHelper helper;
    private SQLiteDatabase db;

    private class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(Context context)
        {
            super( context, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(
                    "CREATE TABLE " + tableName + " ("
                            + username + " TEXT NOT NULL, "
                            + userPassword + " TEXT NOT NULL, "
                            + userEmail + " TEXT NOT NULL "
                            + " )"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion)
        {

        }

    }

    public LocalDatabase( Context context )
    {
        this.context = context;
    }

    private Context getContext(){return this.context;}

    public LocalDatabase open() throws SQLiteException
    {
        this.helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();

        return this;
    }

    public void close()
    {
        helper.close();
    }

    public long addNewUser( String name, String pass, String email)
    {
        ContentValues cv = new ContentValues();
        cv.put(username, name);
        cv.put(userPassword, pass);
        cv.put(userEmail, email);

        return db.insert(tableName, null, cv);
    }

    public boolean userExists()
    {
        try
        {
            String sql = "SELECT " + userPassword + ", " + userPassword +
                    " FROM " + tableName;

            Cursor c = db.rawQuery(sql, null);

            int userPassIndex = c.getColumnIndex( userPassword );

            if( c.getCount() > 0 )
            {
                return true;
            }

        }
        catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public boolean isLegitimateUser( String name, String pass) {
        try
        {
            String sql = "SELECT " + userPassword +
                    " FROM " + tableName +
                    " WHERE " + username + "=?";
            String[] args = new String[]{name};

            Cursor c = db.rawQuery(sql, args);

            int userPassIndex = c.getColumnIndex( userPassword );

            if( c.getCount() > 0 )
            {
                c.moveToFirst();

                String password = c.getString( userPassIndex );

                if( pass.equals( password ) )
                {
                    return true;
                }
                else
                {
                    Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Invalid username!", Toast.LENGTH_SHORT).show();
            }

        }catch(CursorIndexOutOfBoundsException e)
        {
            Toast.makeText(getContext(), "User does not exist!\nPlease sign up", Toast.LENGTH_SHORT).show();
        }
        catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
