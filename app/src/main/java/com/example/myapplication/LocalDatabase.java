package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Logger;

public class LocalDatabase {

    //category data
    private static final String categoryId = "category_id";
    private static final String categoryName = "category_name";
    private static final String categoryTableName = "category_table";

    //productData
    private static final String productId = "product_id";
    private static final String productImageUri = "product_image_uri";
    private static final String productName = "product_name";
    private static final String productDescription = "product_description";
    private static final String productCategory = "product_category";
    private static final String productNumber = "product_quantity_in_stock";
    private static final String productPrice = "product_price_per_piece";
    private static final String productTableName = "product_table";

    //userData
    private static final String username = "username";
    private static final String userPassword = "user_password";
    private static final String userEmail = "user_email";
    private static final String userPhone = "user_phone";
    private static final String userProfilePic = "user_profile_picture";
    private static final String userType = "user_type";

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
                                + userEmail + " TEXT NOT NULL, "
                                + userPhone + " TEXT NOT NULL, "
                                + userProfilePic + " TEXT, "
                                + userType + " BOOLEAN NOT NULL"
                                + " )"
                );
                db.execSQL(
                        "CREATE TABLE " + categoryTableName + " ( "
                                + categoryId + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                                + categoryName + " TEXT NOT NULL"
                                + " )"
                );

                db.execSQL(
                        "CREATE TABLE " + productTableName + " ( "
                                + productId + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                                + productName + " TEXT NOT NULL, "
                                + productImageUri + " TEXT NOT NULL, "
                                + productDescription + " TEXT NOT NULL, "
                                + productCategory + " INTEGER NOT NULL, "
                                + productNumber + " INTEGER NOT NULL, "
                                + productPrice + " INTEGER NOT NULL,"
                                + " FOREIGN KEY (" + productCategory + ")"
                                + " REFERENCES " + categoryTableName + "(" + categoryId + ")"
                                + " )"
                );


                String sql = "INSERT INTO " + categoryTableName + "( " + categoryName + " ) "
                        + " VALUES ('Food'), "
                        + "('Grocery'), "
                        + "('Cutlery'), "
                        + "('Electronics'), "
                        + "('Clothes'), "
                        + "('Other')";
                db.execSQL(sql);

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

    public long addNewUser( String name, String phoneNumber, String pass, String email, boolean user_type)
    {
        ContentValues cv = new ContentValues();
        cv.put(username, name);
        cv.put(userPassword, pass);
        cv.put(userEmail, email);
        cv.put(userPhone, phoneNumber);
        cv.put(userType, user_type);

        return db.insert(tableName, null, cv);
    }

    public String getUserType()
    {
        String type = "";

        try
        {
            String []cols = new String[]{
                    userType
            };

            Cursor c = db.query( tableName, cols, null, null, null, null, null);

            int userTypeIndex = c.getColumnIndex( userType );

            if( c.getCount() > 0 )
            {
                c.moveToFirst();
                type = c.getString( userTypeIndex );
            }
            c.close();
        }catch ( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return type;
    }

    public String[] getUserDetails()
    {
        String []details = new String[3];

        try
        {
            String sql = "SELECT " + username + ", " + userPhone + ", " + userEmail
                    + " FROM " + tableName;

            Cursor c = db.rawQuery( sql, null);

            int nameIndex = c.getColumnIndex( username );
            int phoneIndex = c.getColumnIndex( userPhone );
            int emailIndex = c.getColumnIndex( userEmail );

            if( c.getCount() > 0 )
            {
                c.moveToFirst();
                details[0] = c.getString( nameIndex );
                details[1] = c.getString( phoneIndex );
                details[2] = c.getString( emailIndex );
                c.close();
            }
        }catch (Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return details;
    }

    public boolean userExists()
    {
        try
        {
            String sql = "SELECT " + username + ", " + userPassword + ", " + userType
                    + " FROM " + tableName;

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
            return false;
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

    public long setProfilePicture(String image)
    {
        ContentValues cv = new ContentValues();
        cv.put( userProfilePic, image);

        return db.update( tableName, cv, null, null);
    }

    public Bitmap getProfilePicture()
    {
        Bitmap image = null;

        try
        {
            String sql = "SELECT " + userProfilePic + " FROM " + tableName;

            Cursor c = db.rawQuery( sql, null );

            int profilePicIndex = c.getColumnIndex( userProfilePic );

            if( c.getCount() > 0 )
            {
                c.moveToFirst();
                String imageUri = c.getString( profilePicIndex );

                c.close();

                if( imageUri != null )
                {
                    image = MediaStore.Images.Media.getBitmap( getContext().getContentResolver(), Uri.parse(imageUri));
                }
            }
        }catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return image;
    }

    public long addProduct(String product_image_uri, String product_name, String product_desc, String product_cat, int product_num, int product_price)
    {
        ContentValues cv = new ContentValues();
        cv.put(productImageUri, product_image_uri);
        cv.put(productName, product_name);
        cv.put(productDescription, product_desc);
        cv.put(productCategory, product_cat);
        cv.put(productNumber, product_num);
        cv.put(productPrice, product_price);

        return db.insert(productTableName, null, cv);
    }

    public ArrayList<String[]> getAvailableProducts()
    {
        ArrayList<String[]> products = new ArrayList<>();

        try
        {
            String[] cols = new String[]{
              productImageUri,
              productName,
              productDescription,
              productCategory,
              productNumber,
              productPrice
            };

            Cursor c = db.query( productTableName, cols, null, null, null, null, null);

            int prodImgUriIndex = c.getColumnIndex( productImageUri );
            int prodNameIndex = c.getColumnIndex( productName );
            int prodDescIndex = c.getColumnIndex( productDescription );
            int prodCatIndex = c.getColumnIndex( productCategory );
            int prodNumIndex = c.getColumnIndex( productNumber );
            int prodPriceIndex = c.getColumnIndex( productPrice );

            if( c.getCount() > 0 )
            {
                c.moveToFirst();

                while( !c.isAfterLast() )
                {
                    products.add( new String[]{
                       new String( c.getString( prodImgUriIndex ) ),
                       new String( c.getString( prodNameIndex ) ),
                       new String( c.getString( prodDescIndex ) ),
                       new String( c.getString( prodCatIndex ) ),
                       new String( c.getString( prodNumIndex ) ),
                       new String( c.getString( prodPriceIndex ) )
                    });
                    c.moveToNext();
                }
            }
            c.close();
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return products;
    }

    public ArrayList<String> getAvailableCategories()
    {
        ArrayList<String> list = new ArrayList<>();

        try
        {
            String []cols = new String[]{
              categoryName
            };

            Cursor c = db.query( categoryTableName, cols, null,null, null, null, null);

            int catNameIndex = c.getColumnIndex( categoryName );

            if( c.getCount() > 0 )
            {
                c.moveToFirst();
                while( !c.isAfterLast() )
                {
                    list.add( new String( c.getString( catNameIndex ) ) );
                    c.moveToNext();
                }
            }

        }catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return list;
    }
}
