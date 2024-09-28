package com.example.myapplication;

public class ProductModel {
    private String productImageUri;
    private String productName;
    private String productDescription;
    private String productCategory;
    private long productNumber;
    private long productCost;

    public ProductModel( String productImageUri, String productName, String productDescription, String productCategory, long productNumber, long productCost)
    {
        this.productImageUri = new String(productImageUri);
        this.productName = new String(productName);
        this.productDescription = new String(productDescription);
        this.productCategory = new String(productCategory);
        this.productNumber = productNumber;
        this.productCost = productCost;
    }

    public String getProductImageUri() {
        return productImageUri;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public long getProductNumber() {
        return productNumber;
    }

    public long getProductCost() {
        return productCost;
    }
}
