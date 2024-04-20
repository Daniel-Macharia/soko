package com.example.myapplication;

public class ListItem {

    private int itemImageID;
    private String itemName;
    private String itemDesc;
    private int addToCartIconID;

    public ListItem( int itemImageID, String itemName, String itemDesc, int addToCartIconID)
    {
        this.itemImageID = itemImageID;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.addToCartIconID = addToCartIconID;
    }

    public void setItemImageID( int itemImageID )
    {
        this.itemImageID = itemImageID;
    }
    public void setItemName( String itemName )
    {
        this.itemName = new String(itemName);
    }
    public void setDesc( String desc )
    {
        this.itemDesc = new String( desc );
    }

    public void setAddToCartIconID( int addToCartIconID )
    {
        this.addToCartIconID = addToCartIconID;
    }

    public int getItemImageID(){return this.itemImageID;}

    public String getItemName(){return this.itemName;}
    public String getDesc(){ return this.itemDesc;}

    public int getAddToCartIconID(){return this.addToCartIconID;}
}
