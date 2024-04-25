package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

                LocalDatabase db = new LocalDatabase(getApplicationContext());
                db.open();
                db.addProduct(productPictureUri, name, desc, cart, Integer.parseInt(num), Integer.parseInt(price));
                db.close();

                MyShopActivity.adapter.addRefreshList( new ProductModel(productPictureUri,
                        name,
                        desc,
                        cart,
                        Integer.parseInt(num),
                        Integer.parseInt(price)));

                clearInputFields();
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
}
