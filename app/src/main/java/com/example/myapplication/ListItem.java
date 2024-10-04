package com.example.myapplication;

public class ListItem {

    private int itemImageID;
    private String itemName;
    private String itemDesc;
    private double itemPrice;
    private long itemQuantityInStock;
    private int addToCartIconID;

    private String sellerEmail, sellerPhone, shopName;

    public ListItem( int itemImageID, String itemName, String itemDesc, int addToCartIconID, double itemPrice, long itemQuantityInStock, String sellerEmail, String sellerPhone, String shopName)
    {
        this.itemImageID = itemImageID;
        this.itemName = new String(itemName);
        this.itemDesc = new String(itemDesc);
        this.addToCartIconID = addToCartIconID;
        this.itemPrice = itemPrice;
        this.itemQuantityInStock = itemQuantityInStock;

        this.sellerEmail = new String( sellerEmail );
        this.sellerPhone = new String( sellerPhone );
        this.shopName = new String( shopName );
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

    public void setSellerEmail( String sellerEmail ){this.sellerEmail = new String( sellerEmail );}
    public void setSellerPhone( String sellerPhone ){this.sellerPhone = new String( sellerPhone );}

    public String getSellerEmail(){return this.sellerEmail;}
    public String getSellerPhone(){return this.sellerPhone;}

    public void setShopName( String shopName ){this.shopName = new String( shopName );}

    public String getShopName(){return this.shopName;}
}
