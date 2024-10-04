package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyShopActivity extends AppCompatActivity {

    private ImageView menu, profile;
    private GridView listItems;
    private ArrayList<ProductModel> myProducts = new ArrayList<>();
    public static ProductModelAdapter adapter;

    private LinearLayout addProductLayout;
    private TextView screenTitle;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_shop_activity );


        addProductLayout = findViewById( R.id.addProduct );
        screenTitle = findViewById( R.id.screenTitle );

        screenTitle.setText("My Shop");

        menu = findViewById( R.id.main_menu );
        profile = findViewById( R.id.profile );

        listItems = findViewById( R.id.my_shop_item_list );

        requestPermissions();
        setGridItems();

        adapter = new ProductModelAdapter(getApplicationContext(), myProducts);
        listItems.setAdapter( adapter );
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMainMenu( MyShopActivity.this, v);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isFromShop = "shop";
                Intent intent = new Intent( MyShopActivity.this, ProfileActivity.class );
                intent.putExtra("isFromShop", isFromShop);

                Intent myShopIntent = getIntent();
                intent.putExtra("userName", myShopIntent.getStringExtra("userName"));
                intent.putExtra("userEmail", myShopIntent.getStringExtra("userEmail"));
                intent.putExtra("userPhone", myShopIntent.getStringExtra("userPhone"));
                intent.putExtra("userLocation", myShopIntent.getStringExtra("userLocation"));
                intent.putExtra("userIsSeller", myShopIntent.getBooleanExtra("userIsSeller", false));

                startActivity( intent );
            }
        });

        addProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyShopActivity.this, "Add new Product!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyShopActivity.this, AddProductActivity.class );

                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));

                startActivity(intent);
            }
        });
    }

    private void setGridItems()
    {
        /*LocalDatabase db = new LocalDatabase(getApplicationContext());
        db.open();
        ArrayList<String[]> products = db.getAvailableProducts();
        db.close();

        for( String[] product: products )
        {
            myProducts.add( new ProductModel(
               product[0],
               product[1],
               product[2],
               product[3],
               Integer.parseInt(product[4]),
               Integer.parseInt(product[5])
            ));
        } */

        String userEmail = getIntent().getStringExtra("userEmail");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("seller");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                try {
                    for( DataSnapshot seller : task.getResult().getChildren() )
                    {
                        String sellerEmail = "";

                        if( seller.hasChild("userEmail") )
                        {
                            sellerEmail = seller.child("userEmail").getValue().toString();
                        }

                        //add only products sold by other people
                        if( userEmail.equals(sellerEmail) )
                        {
                            if( seller.hasChild("products") )
                            {
                                DatabaseReference productsRef = seller.child("products").getRef();

                                productsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        for( DataSnapshot product : task.getResult().getChildren() )
                                        {
                                            String itemName = "", itemDescription = "", itemCartegory = "", sellerMail = "", sellerPhone = "", shopName = "";
                                            long itemPrice = 0;
                                            long itemQuantityInStock = 0;

                                            if( product.hasChild("productName") )
                                            {
                                                itemName = product.child("productName").getValue().toString();
                                            }
                                            if( product.hasChild("productDescription") )
                                            {
                                                itemDescription = product.child("productDescription").getValue().toString();
                                            }
                                            if( product.hasChild("productPrice") )
                                            {
                                                itemPrice = (long) product.child("productPrice").getValue();
                                            }
                                            if( product.hasChild("productQuantity") )
                                            {
                                                itemQuantityInStock = (long) product.child("productQuantity").getValue();
                                            }
                                            if( product.hasChild("productCartegory") )
                                            {
                                                itemCartegory = product.child("productCartegory").getValue().toString();
                                            }

                                            if( seller.hasChild("userEmail") )
                                            {
                                                sellerMail = seller.child("userEmail").getValue().toString();
                                            }

                                            if( seller.hasChild("userPhone") )
                                            {
                                                sellerPhone = seller.child("userPhone").getValue().toString();
                                            }

                                            if( seller.hasChild("shopName") )
                                            {
                                                shopName = seller.child("shopName").getValue().toString();
                                            }

                                            myProducts.add( new ProductModel("", itemName, itemDescription, itemCartegory, itemQuantityInStock, itemPrice) );
                                            //items.add( new ListItem(0, itemName, itemDescription, 0, itemPrice, itemQuantityInStock, sellerMail, sellerPhone, shopName) );
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                    }
                }catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setMainMenu(Context context, View view)
    {
        PopupMenu menu = new PopupMenu( context, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                int id = item.getItemId();

                if( title.equals("Soko") )
                {
                    String startedFromMyShop = "shop";
                    Intent intent = new Intent(MyShopActivity.this, HomeActivity.class );
                    intent.putExtra("startedFromMyShop", startedFromMyShop);

                    Intent myShopIntent = getIntent();
                    intent.putExtra("userName", myShopIntent.getStringExtra("userName"));
                    intent.putExtra("userEmail", myShopIntent.getStringExtra("userEmail"));
                    intent.putExtra("userPhone", myShopIntent.getStringExtra("userPhone"));
                    intent.putExtra("userLocation", myShopIntent.getStringExtra("userLocation"));
                    intent.putExtra("userIsSeller", myShopIntent.getBooleanExtra("userIsSeller", false));

                    startActivity(intent);
                    Toast.makeText(MyShopActivity.this, "Soko", Toast.LENGTH_SHORT).show();
                }

                if( id == R.id.menu_item_notification )
                {
                    String startedFromMyShop = "shop";
                    Intent intent = new Intent( MyShopActivity.this, NotificationActivity.class );
                    intent.putExtra("startedFromMyShop", startedFromMyShop);
                    startActivity( intent );

                    Toast.makeText(MyShopActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else
                if( id == R.id.menu_item_shopping_cart )
                {
                    String startedFromMyShop = "shop";
                    Intent intent = new Intent( MyShopActivity.this, MyShoppingCartActivity.class );
                    intent.putExtra("startedFromMyShop", startedFromMyShop);
                    startActivity( intent );

                    Toast.makeText(MyShopActivity.this, "Shopping cart", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else
                if( id == R.id.menu_item_help )
                {
                    String startedFromMyShop = "shop";
                    Intent intent = new Intent( MyShopActivity.this, HelpActivity.class );
                    intent.putExtra("startedFromMyShop", startedFromMyShop);
                    startActivity( intent );

                    Toast.makeText(MyShopActivity.this, "Help", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        menu.inflate(R.menu.main_menus );
        menu.getMenu().add("Soko");
        menu.show();

    }

    @Override
    public void onResume()
    {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    private void requestPermissions()
    {
        if(ActivityCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions( MyShopActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

}
