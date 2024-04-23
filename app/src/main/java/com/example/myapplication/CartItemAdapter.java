package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CartItemAdapter extends ArrayAdapter<CartItem> {

    public CartItemAdapter(Context context, ArrayList<CartItem> list )
    {
        super( context, 0, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent )
    {
        View currentView = convertView;

        if( currentView == null )
        {
            currentView = LayoutInflater.from( getContext() ).inflate(R.layout.cart_item, parent, false);
        }

        return currentView;
    }
}
