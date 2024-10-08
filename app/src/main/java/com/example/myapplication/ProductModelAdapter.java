package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

            productImage.setImageResource(R.drawable.downloading);
            productName.setText( currentItem.getProductName() );
            productPrice.setText( "Ksh " + currentItem.getProductCost() + "/=" );

            setItemImage( productImage, currentItem.getProductImageUri() );

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

    private void setItemImage( ImageView image, String itemImageUri)
    {
        try{
            String path = Uri.parse(itemImageUri).getLastPathSegment();

            if( path == null )
            {
                path = "";
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReference().child(path);

            imageRef.getBytes( 2 * 1024 * 1024 )//read a max of 2 megabytes
                    .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                        @Override
                        public void onComplete(@NonNull Task<byte[]> task) {
                            try
                            {
                                byte[] imageData = task.getResult();

                                Bitmap bm = BitmapFactory.decodeByteArray( imageData, 0, imageData.length);
                                image.setImageBitmap(bm);
                            }catch( Exception e )
                            {
                                toast("Error: " + e);
                            }
                        }
                    });

        }catch( Exception e )
        {
            toast("Error: " + e );
        }
    }

    private void toast(String message)
    {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
