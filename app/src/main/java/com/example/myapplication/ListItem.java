package com.example.myapplication;

public class ListItem {

    private int itemImageID;
    private String itemName;
    private String itemDesc;
    private double itemPrice;
    private long itemQuantityInStock;
    private int addToCartIconID;

    public ListItem( int itemImageID, String itemName, String itemDesc, int addToCartIconID, double itemPrice, long itemQuantityInStock)
    {
        this.itemImageID = itemImageID;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.addToCartIconID = addToCartIconID;
        this.itemPrice = itemPrice;
        this.itemQuantityInStock = itemQuantityInStock;
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

    public void setItemPrice(double itemPrice){this.itemPrice = itemPrice;}
    public double getItemPrice(){return this.itemPrice;}

    public void setItemQuantityInStock(int itemQuantityInStock){this.itemQuantityInStock = itemQuantityInStock;}
    public long getItemQuantityInStock(){return this.itemQuantityInStock;}
}
