package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {

    private LinearLayout phone, mail;
    private TextView shopName, productName, productDesc, productPrice;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.product_activity );

        phone = findViewById( R.id.phone );
        mail = findViewById( R.id.mail );

        shopName = findViewById( R.id.shopName );
        productName = findViewById( R.id.productName );
        productPrice = findViewById( R.id.productPrice );
        productDesc = findViewById( R.id.productDescription );

        String shop_name, product_name, product_desc, product_price, product_quantity, sellerEmail, sellerPhone;

        Intent intent = getIntent();

        shop_name = intent.getStringExtra("shopName");
        product_name = intent.getStringExtra("productName");
        product_desc = intent.getStringExtra("productDescription");
        product_price = intent.getStringExtra("productPrice");
        product_quantity = intent.getStringExtra( "quantityInStock");

        sellerEmail = intent.getStringExtra("sellerEmail");
        sellerPhone = intent.getStringExtra("sellerPhone");

        shopName.setText(shop_name);
        productName.setText(product_name);
        productDesc.setText(product_desc);
        productPrice.setText(product_price);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSeller(sellerPhone);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder builder = new StringBuilder("");

                builder.append("Name: " + product_name + "\n");
                builder.append("Description: " + product_desc + "\n");
                builder.append("Price: " + product_price + "\n");
                emailSeller(sellerEmail, builder.toString());
            }
        });

    }

    private void callSeller(String sellerPhone)
    {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + sellerPhone));

            if(ActivityCompat.checkSelfPermission( ProductActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED )
            {
                ActivityCompat.requestPermissions(ProductActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            }

            startActivity( callIntent );

            Toast.makeText(getApplicationContext(), "Calling seller...", Toast.LENGTH_SHORT).show();
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void emailSeller(String sellerEmail, String productData)
    {
        try {
            String mailSubject = "product requisition.";
            String mailBody = "I am writing in requisition of the product: \n" + productData;
            Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
            selectorIntent.setData(Uri.parse("mailto:"));

            Intent emailIntent = new Intent(Intent.ACTION_SEND );
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{sellerEmail});
            emailIntent.putExtra( Intent.EXTRA_SUBJECT, mailSubject);
            emailIntent.putExtra( Intent.EXTRA_TEXT, mailBody);
            emailIntent.setSelector( selectorIntent );

            startActivity( Intent.createChooser( emailIntent, "Send Email") );

            Toast.makeText(getApplicationContext(), "Sending seller and email...", Toast.LENGTH_SHORT).show();
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }
}
