package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView menu, profile;
    private GridView listItems;
    private ArrayList<ListItem> items = new ArrayList<>();

    private MyListAdapter adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home_activity );

        menu = findViewById( R.id.main_menu );
        profile = findViewById( R.id.profile );

        listItems = findViewById( R.id.item_list );

        requestPermissions();
        setGridItems();

        adapter = new MyListAdapter(getApplicationContext(), items);
        listItems.setAdapter( adapter );

        if(ActivityCompat.checkSelfPermission( HomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        }

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                intent.putExtra("productName", items.get(i).getItemName() );
                intent.putExtra("productDescription", items.get(i).getDesc() );
                intent.putExtra("productPrice", "" + items.get(i).getItemPrice() );
                intent.putExtra("quantityInStock", items.get(i).getItemQuantityInStock() );
                intent.putExtra("sellerEmail", items.get(i).getSellerEmail() );
                intent.putExtra("sellerPhone", items.get(i).getSellerPhone() );
                intent.putExtra("shopName", items.get(i).getShopName() );

                intent.putExtra("productImageUri", items.get(i).getItemImageUri() );

                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String startedFromMyShop = intent.getStringExtra("startedFromMyShop");

        if( startedFromMyShop != null && startedFromMyShop.equals("shop") )
        {
            menu.setImageDrawable( getDrawable(R.drawable.home_icon) );
            profile.setImageDrawable( getDrawable( R.drawable.logout_icon));
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( HomeActivity.this, LoginActivity.class );

                    /*Intent homeIntent = getIntent();
                    intent.putExtra("userName", homeIntent.getStringExtra("userName"));
                    intent.putExtra("userEmail", homeIntent.getStringExtra("userEmail"));
                    intent.putExtra("userPhone", homeIntent.getStringExtra("userPhone"));
                    intent.putExtra("userLocation", homeIntent.getStringExtra("userLocation"));
                    intent.putExtra("userIsSeller", homeIntent.getBooleanExtra("userIsSeller", true));
                     */

                    startActivity( intent );

                    finishAffinity();
                }
            });
        }
        else
        {
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMainMenu( HomeActivity.this, v);
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( HomeActivity.this, ProfileActivity.class );

                    Intent homeIntent = getIntent();
                    intent.putExtra("userName", new String( homeIntent.getStringExtra("userName") ) );
                    intent.putExtra("userEmail", new String( homeIntent.getStringExtra("userEmail") ) );
                    intent.putExtra("userPhone", new String( homeIntent.getStringExtra("userPhone") ) );
                    intent.putExtra("userLocation", new String( homeIntent.getStringExtra("userLocation") ) );
                    intent.putExtra("userIsSeller", homeIntent.getBooleanExtra("userIsSeller", true));

                    startActivity( intent );
                }
            });
        }


    }

    private boolean contains(ArrayList<ListItem> products, ListItem product )
    {
        for( ListItem item : products )
        {
            if( item.getItemImageUri().equals(product.getItemImageUri() )
                    && item.getItemName().equals( product.getItemName() )
                    && item.getSellerEmail().equals( product.getSellerEmail()) )
            {
                return true;
            }
        }

        return false;
    }

    private void setGridItems()
    {
        String userEmail = ( (getIntent().getStringExtra("userEmail") == null) ? "" : getIntent().getStringExtra("userEmail") );

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("seller");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sellerSnapshot) {
                try {
                    for( DataSnapshot seller : sellerSnapshot.getChildren() )
                    {
                        String sellerEmail = "";

                        if( seller.hasChild("userEmail") )
                        {
                            sellerEmail = seller.child("userEmail").getValue().toString();
                        }

                        //add only products sold by other people
                        if( !userEmail.equals(sellerEmail) )
                        {
                            if( seller.hasChild("products") )
                            {
                                DatabaseReference productsRef = seller.child("products").getRef();

                                productsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                                        try {
                                            for( DataSnapshot product : productSnapshot.getChildren() )
                                            {
                                                String itemName = "", itemImageUri = "", itemDescription = "", sellerMail = "", sellerPhone = "", shopName = "";
                                                long itemPrice = 0;
                                                long itemQuantityInStock = 0;

                                                if( product.hasChild("productImageUri") )
                                                {
                                                    itemImageUri = product.child("productImageUri").getValue().toString();
                                                }

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

                                                ListItem item = new ListItem(itemImageUri, itemName, itemDescription, 0, itemPrice, itemQuantityInStock, sellerMail, sellerPhone, shopName);

                                                if( !contains( items, item) )
                                                {
                                                    items.add( item );
                                                }

                                                adapter.notifyDataSetChanged();
                                            }
                                            adapter.notifyDataSetChanged();
                                        }catch(Exception e)
                                        {
                                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setMainMenu(Context context, View view)
    {
        PopupMenu menu = new PopupMenu( context, view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                if( id == R.id.menu_item_notification )
                {
                    Intent intent = new Intent( HomeActivity.this, NotificationActivity.class );
                    startActivity( intent );

                    Toast.makeText(HomeActivity.this, "Clicked Notifications", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else
                if( id == R.id.menu_item_shopping_cart )
                {
                    Intent intent = new Intent( HomeActivity.this, MyShoppingCartActivity.class );
                    startActivity( intent );

                    Toast.makeText(HomeActivity.this, "Clicked shopping cart", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else
                if( id == R.id.menu_item_help )
                {
                    Intent intent = new Intent( HomeActivity.this, HelpActivity.class );
                    startActivity( intent );

                    Toast.makeText(HomeActivity.this, "Clicked Help", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        menu.inflate(R.menu.main_menus );
        menu.show();

    }

    private void requestPermissions()
    {
        if(ActivityCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions( HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

}
