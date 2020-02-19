package com.example.donation.ModelClasses;

public class UserInformation {

    public String name;
    public String address;
    public String phonenumber;
    public String latitude;
    public String longitude;
    public String category;

    public UserInformation(){

    }
    public UserInformation(String name,String address, String phonenumber, String latitude, String longitude, String category) {
        this.name=name;
        this.address=address;
        this.phonenumber=phonenumber;
        this.latitude=latitude;
        this.longitude=longitude;
        this.category=category;
    }
}
