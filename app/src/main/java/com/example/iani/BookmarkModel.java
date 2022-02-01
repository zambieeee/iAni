package com.example.iani;

public class BookmarkModel {

    private String productImage;
    private String productTitle;
    private String rating;
    private long totalRatings;
    private String productPrice;
    private boolean COD;

    public BookmarkModel(String productImage, String productTitle, String rating, long totalRatings, String productPrice, boolean COD) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.productPrice = productPrice;
        this.COD = COD;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }
}
