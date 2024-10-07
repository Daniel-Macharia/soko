package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        TextView price = currentView.findViewById(R.id.price );

        image.setImageResource( R.drawable.downloading );
        desc.setText(item.getItemName());
        price.setText( "ksh " + item.getItemPrice() + "/=");

        setItemImage(image, item.getItemImageUri() );


        return currentView;

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
}
