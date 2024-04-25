package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ImageView homeIcon, logoutIcon;
    private TextView screenTitle;
    private ListView notificationList;
    private ArrayList<NotificationItem> notificationItemList = new ArrayList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.notification_activity );

        homeIcon = findViewById( R.id.home_icon );
        logoutIcon = findViewById( R.id.logout_icon );

        screenTitle = findViewById( R.id.screenTitle );
        screenTitle.setText("Notifications");

        notificationList = findViewById( R.id.notification_item_list );

        setNotificationItemList();

        NotificationItemAdapter adapter = new NotificationItemAdapter( getApplicationContext(), notificationItemList);
        notificationList.setAdapter( adapter );

        homeIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent;

                Intent i = getIntent();
                String startedFromMyShop = i.getStringExtra("startedFromMyShop");
                if( startedFromMyShop != null && startedFromMyShop.equals("shop") )
                {
                    intent = new Intent( NotificationActivity.this, MyShopActivity.class );
                }
                else
                {
                    intent = new Intent( NotificationActivity.this, HomeActivity.class );
                }

                startActivity(intent);

                finishAffinity();
            }
        });

        logoutIcon.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( NotificationActivity.this, LoginActivity.class );
                startActivity(intent);

                finishAffinity();
            }
        });
    }

    private void setNotificationItemList()
    {
        for( int i = 0; i < 20; i++ )
        {
            notificationItemList.add( new NotificationItem("", "") );
        }
    }
}
