package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductModelAdapter extends ArrayAdapter<ProductModel> {

    private ArrayList<ProductModel> list = new ArrayList<>();
    private Context context;

    public ProductModelAdapter(Context context, ArrayList<ProductModel> list)
    {
        super( context, 0, list);
        this.context = context;
        this.list = list;
    }

    private Context getAppContext(){return this.context;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View currentView = convertView;

        try
        {
            if( currentView == null )
            {
                currentView = LayoutInflater.from(getContext()).inflate(R.layout.shop_product, parent, false);
            }

            ProductModel currentItem = getItem( position );

            assert currentItem != null;
            ImageView productImage = currentView.findViewById( R.id.product_image );
            ImageView productRemove = currentView.findViewById( R.id.remove_product );
            TextView productName = currentView.findViewById( R.id.product_name );
            TextView productPrice = currentView.findViewById( R.id.product_price );


            productImage.setImageBitmap(MediaStore.Images.Media.getBitmap( getAppContext().getContentResolver(), Uri.parse( currentItem.getProductImageUri() ) ));
            productName.setText( currentItem.getProductName() );
            productPrice.setText( "Ksh " + currentItem.getProductCost() + "/=" );

            productRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getAppContext(), "Remove product: " + productName.getText().toString(), Toast.LENGTH_SHORT).show();
                    removeRefreshList( position );
                }
            });

        }catch( Exception e )
        {
            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }

        return currentView;
    }

    public void addRefreshList(ProductModel newProduct)
    {
        list.add( newProduct );
        notifyDataSetChanged();
    }
    public void removeRefreshList( int itemPosition)
    {
        list.remove( itemPosition );
        notifyDataSetChanged();
    }
}
