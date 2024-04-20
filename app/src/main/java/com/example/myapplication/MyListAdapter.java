package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    public MyListAdapter( Context context, ArrayList<ListItem> items)
    {
        super(context, 0, items);
        this.context = context;
    }

    public Context getAppContext(){return this.context;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View currentView = convertView;

        if( currentView == null )
        {
            currentView = LayoutInflater.from(getContext()).inflate( R.layout.list_item, parent, false);
        }

        ListItem item = getItem( position );

        assert item != null;

        ImageView image = currentView.findViewById( R.id.list_item_image);
        TextView desc = currentView.findViewById(R.id.desc );

        image.setImageResource( R.drawable.food_item );
        desc.setText(item.getDesc());


        return currentView;

    }
}
