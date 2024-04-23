package com.example.myapplication;

public class CartItem {

    private String itemName;

    public CartItem( String itemName )
    {
        this.itemName = itemName;
    }

    public void setItemName( String itemName ){this.itemName = itemName;}

    public String getItemName(){return this.itemName;}
}
