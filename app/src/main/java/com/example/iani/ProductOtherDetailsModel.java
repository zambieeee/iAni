package com.example.iani;

public class ProductOtherDetailsModel {

    private String featureName;
    private String featureValue;
    private int type;

    public ProductOtherDetailsModel( int type, String featureName, String featureValue) {
        this.featureName = featureName;
        this.featureValue = featureValue;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }
}
