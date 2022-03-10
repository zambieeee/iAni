package com.example.iani;

public class CartItemModel {
    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //cart item
    private String productID;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private Long productQuantity;
    private boolean inStock;

    public CartItemModel(int type, String productID, String productImage, String productTitle, String productPrice, Long productQuantity,boolean inStock) {
        this.type = type;
        this.productID =productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.inStock = inStock;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }
    //cart item

    //cart total

    public CartItemModel(int type) {
        this.type = type;
    }


    //cart total
}
