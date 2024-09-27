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
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView menu, profile;
    private GridView listItems;
    private ArrayList<ListItem> items = new ArrayList<>();

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

        MyListAdapter adapter = new MyListAdapter(getApplicationContext(), items);
        listItems.setAdapter( adapter );

        Intent intent = getIntent();
        String startedFromMyShop = intent.getStringExtra("startedFromMyShop");

        if( startedFromMyShop != null && startedFromMyShop.equals("shop") )
        {
            menu.setImageDrawable( getDrawable(R.drawable.home_icon) );
            profile.setImageDrawable( getDrawable( R.drawable.logout_icon));
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, MyShopActivity.class );
                    startActivity( intent );

                    finishAffinity();
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( HomeActivity.this, LoginActivity.class );

                    Intent homeIntent = getIntent();
                    intent.putExtra("userName", homeIntent.getStringExtra("userName"));
                    intent.putExtra("userEmail", homeIntent.getStringExtra("userEmail"));
                    intent.putExtra("userPhone", homeIntent.getStringExtra("userPhone"));
                    intent.putExtra("userLocation", homeIntent.getStringExtra("userLocation"));
                    intent.putExtra("userIsSeller", homeIntent.getBooleanExtra("userIsSeller", true));

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
                    startActivity( intent );
                }
            });
        }


    }

    private void setGridItems()
    {
        for( int i = 0; i <= 20; i++)
        {
            items.add( new ListItem( 0, "Item Name", "Item " + i, 0) );
        }
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
