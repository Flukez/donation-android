package com.example.donation.ModelClasses;

public class UserInformation {

    public String name;
    public String adress;
    public String phonenumber;
    public double latitude;
    public double longitude;

    public UserInformation(){

    }
    public UserInformation(String name,String adress, String phonenumber, double latitude, double longitude) {
        this.name=name;
        this.adress=adress;
        this.phonenumber=phonenumber;
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
