package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class NotificationItemAdapter extends ArrayAdapter<NotificationItem> {
    private Context context;

    public NotificationItemAdapter(Context context, ArrayList<NotificationItem> list)
    {
        super(context, 0, list);
        this.context = context;
    }

    public Context getAppContext(){return this.context;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View currentView = convertView;

        if( currentView == null )
        {
            currentView = LayoutInflater.from( getContext() ).inflate(R.layout.notification_item, parent, false);
        }
        //NotificationItem item = getItem( position );


        return currentView;
    }

}
