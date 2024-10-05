package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyShoppingCartActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon;

    private TextView screenTitle;

    private ListView cartList;

    private ArrayList<CartItem> cartItemList = new ArrayList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_shopping_cart );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        screenTitle = findViewById( R.id.screenTitle );
        screenTitle.setText("My Items");

        cartList = findViewById( R.id.cart_item_list );

        setCartItemList();

        CartItemAdapter adapter = new CartItemAdapter( getApplicationContext(), cartItemList );
        cartList.setAdapter( adapter );

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
                Intent intent = new Intent( MyShoppingCartActivity.this, LoginActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });

    }

    private void setCartItemList()
    {
        for( int i = 0; i< 20; i++)
        {
            cartItemList.add( new CartItem("") );
        }
    }
}
