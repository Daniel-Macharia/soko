package com.example.myapplication;

public class ProductModel {
    String productImageUri;
    String productName;
    String productDescription;
    String productCategory;
    int productNumber;
    int productCost;

    public ProductModel( String productImageUri, String productName, String productDescription, String productCategory, int productNumber, int productCost)
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

    public int getProductNumber() {
        return productNumber;
    }

    public int getProductCost() {
        return productCost;
    }
}
