package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon, productPicture;
    private TextView screenTitle;
    private EditText productName, productDescription, productNumber, productPrice;
    private Spinner productCart;
    private ArrayList<String> categoryList = new ArrayList<>();

    private Button addProduct;

    private String productPictureUri = "";

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if( result.getResultCode() == Activity.RESULT_OK )
                {
                    Intent data = result.getData();

                    if( data != null && data.getData() != null )
                    {
                        Uri selectedImageUri = data.getData();

                        Bitmap selectedImageBitmap;

                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri
                            );

                            productPictureUri = new String(selectedImageUri.toString());
                            productPicture.setImageBitmap( selectedImageBitmap );

                        }catch ( Exception e )
                        {
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_product_activity );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );
        productPicture = findViewById( R.id.product_picture );

        screenTitle = findViewById( R.id.screenTitle );
        screenTitle.setText("Add Product");

        productName = findViewById( R.id.product_name );
        productDescription = findViewById( R.id.product_description );
        productNumber = findViewById( R.id.product_number );
        productPrice = findViewById( R.id.product_price );
        productCart = (Spinner)findViewById( R.id.product_cart );

        addProduct = findViewById( R.id.add_product );

        initCategoryItems();

        ArrayAdapter adapter = new ArrayAdapter( this, R.layout.spinner_text_layout, categoryList);
        adapter.setDropDownViewResource( R.layout.spinner_text_layout );
        productCart.setAdapter( adapter );


        homeIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( AddProductActivity.this, MyShopActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( AddProductActivity.this, LoginActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

        productPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, desc, num, price, cart;

                name = productName.getText().toString();
                desc = productDescription.getText().toString();
                num = productNumber.getText().toString();
                price = productPrice.getText().toString();
                cart = productCart.getSelectedItem().toString();

                if( productPictureUri.equals("") )
                {
                    Toast.makeText(AddProductActivity.this, "Please add a picture of the product", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( UtilityClass.isConnectedToInternet( getApplicationContext(), new Handler( Looper.getMainLooper() ) ))
                {
                    int numInStock = Integer.parseInt(num);
                    int productPrice = Integer.parseInt(price);
                    //add product to local db
                    LocalDatabase db = new LocalDatabase(getApplicationContext());
                    db.open();
                    db.addProduct(productPictureUri, name, desc, cart, numInStock, productPrice);
                    db.close();

                    //add product to cloud storage
                    addProductToSoko(productPictureUri, name, desc, cart, numInStock, productPrice);

                    //update the grid view in the main screen
                    MyShopActivity.adapter.addRefreshList( new ProductModel(productPictureUri,
                            name,
                            desc,
                            cart,
                            numInStock,
                            productPrice));

                    clearInputFields();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not connected to the internet!" +
                            "\nPlease check your connection and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        launcher.launch( intent );
    }

    private void clearInputFields()
    {
        productPicture.setImageDrawable(getResources().getDrawable(R.drawable.add_picture, null) );
        productName.setText("");
        productDescription.setText("");
        productNumber.setText("");
        productPrice.setText("");
    }

    private void initCategoryItems()
    {
        LocalDatabase db = new LocalDatabase(getApplicationContext());
        db.open();
        ArrayList<String> list = db.getAvailableCategories();
        db.close();

        for( String cat : list )
            categoryList.add( "" + cat );
    }

    public void addProductToSoko(String productImageUri
            , String productName, String productDescription
            , String productCartegory, int quantity, int price)
    {
        Product product = new Product(productImageUri, productName
                , productDescription, productCartegory, quantity, price);
        Toast.makeText(getApplicationContext(), "Created product. Now adding to firebase...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Toast.makeText(getApplicationContext(), "Creating database instance", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();

                    Toast.makeText(getApplicationContext(), "Getting database reference", Toast.LENGTH_SHORT).show();
                    DatabaseReference ref = db.getReference();

                    Toast.makeText(getApplicationContext(), "Adding product", Toast.LENGTH_SHORT).show();
                    ref.child("product").push().setValue(product);
                }catch( Exception e )
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

}

class Product{
    public String productImageUri, productName, productDescription, productCartegory;
    public int productQuantity, productPrice;
    public Product( String productImageUri, String productName
            , String productDescription, String productCartegory
            , int quantity, int price)
    {
        this.productImageUri = new String(productImageUri);
        this.productName = new String(productName);
        this.productDescription = new String(productDescription);
        this.productCartegory = new String(productCartegory);
        this.productQuantity = quantity;
        this.productPrice = price;
    }
}
