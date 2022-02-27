package com.my.ecommerce.models;

public class UserInfo {

    public String fullName, email, city, country, addressOne, addressTwo, zip;
    public UserType userType;

    public UserInfo(){

    }

    public UserInfo(String fullName, String email, String city, String country, String addressOne, String addressTwo, String zip, UserType userType) {
        this.fullName = fullName;
        this.email = email;
        this.city = city;
        this.country = country;
        this.addressOne = addressOne;
        this.addressTwo = addressTwo;
        this.zip = zip;
        this.userType = userType;
    }
}
