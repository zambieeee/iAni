package com.example.iani;

public class AddressesModel {

    private String fullname;
    private String address;
    private String zipcode;
    private Boolean selected;

    public AddressesModel(String fullname, String address, String zipcode, Boolean selected){
        this.fullname = fullname;
        this.address = address;
        this.zipcode = zipcode;
        this.selected = selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
