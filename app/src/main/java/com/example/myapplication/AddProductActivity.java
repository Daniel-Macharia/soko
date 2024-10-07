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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
                finish();
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

                if( UtilityClass.isNetworkConnectionAvailable( getApplicationContext(), new Handler( Looper.getMainLooper() ) ))
                {
                    int numInStock = Integer.parseInt(num);
                    int productPrice = Integer.parseInt(price);
                    //add product to local db
                  /*  LocalDatabase db = new LocalDatabase(getApplicationContext());
                    db.open();
                    db.addProduct(productPictureUri, name, desc, cart, numInStock, productPrice);
                    db.close(); */

                    String productSellerEmail = getIntent().getStringExtra("userEmail");
                    //add product to cloud storage
                    Toast.makeText(getApplicationContext(), "Adding product using email: " + productSellerEmail, Toast.LENGTH_SHORT).show();
                    addProductToSoko( new String(productSellerEmail), productPictureUri, name, desc, cart, numInStock, productPrice);

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

    public void addProductToSoko(String productSellerEmail, String productImageUri
            , String productName, String productDescription
            , String productCartegory, int quantity, int price)
    {
        try {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("seller");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            /*new Thread(new Runnable() {
                @Override
                public void run() {*/

            StorageReference itemStorageRef = storageReference.child(productSellerEmail + "/" + Uri.parse(productImageUri).getLastPathSegment() );

            UploadTask task = itemStorageRef.putFile(Uri.parse(productImageUri));
                    try{
                        //add image to firebase storage bucket
                                Task<Uri> urlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if( task.isSuccessful() )
                                        {
                                            //image successfully uploaded to firebase storage
                                            toast("Successfully uploaded product image..");
                                            return itemStorageRef.getDownloadUrl();
                                        }
                                        else
                                        {
                                            toast("Could not upload product image!");
                                            return null;
                                        }
                                    }
                                });

                                urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        Uri downloadUri = task.getResult();

                                        if( downloadUri == null )
                                        {
                                            toast("Could not add product!." +
                                                    "\nPlease check your connection and try again.");
                                        }
                                        else
                                        {
                                            Product product = new Product(new String(downloadUri.toString()), productName, productDescription
                                                    , productCartegory, quantity, price);
                                            addProductData(ref, productSellerEmail, product);
                                        }
                                    }
                                });

                    }catch( Exception e )
                    {
                        toast("Error: " + e);
                    }
                /*}
            });.start();*/
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void addProductData(DatabaseReference ref, String productSellerEmail, Product product)
    {
        try {
            //add product data to realtime database here.
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String sellerMail = "";
                    for( DataSnapshot seller : task.getResult().getChildren() )
                    {
                        if( seller.hasChild("userEmail") )
                        {
                            sellerMail = seller.child("userEmail").getValue().toString();
                        }

                        if( sellerMail.equals(productSellerEmail) )
                        {
                            //if( seller.hasChild("shopProducts") )
                            {
                                DatabaseReference productRef = seller.child("products").getRef();

                                productRef.push().setValue(product);
                            }

                            return;
                        }
                    }
                }
            });
        }catch( Exception e )
        {
            toast("Error: " + e);
        }
    }

    private void toast(String message)
    {
        new Handler( Looper.getMainLooper() ).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

class Product{
    public String productImageUri, productName, productDescription, productCartegory;
    public long productQuantity, productPrice;
    public Product( String productImageUri, String productName
            , String productDescription, String productCartegory
            , long quantity, long price)
    {
        this.productImageUri = new String(productImageUri);
        this.productName = new String(productName);
        this.productDescription = new String(productDescription);
        this.productCartegory = new String(productCartegory);
        this.productQuantity = quantity;
        this.productPrice = price;
    }
}
