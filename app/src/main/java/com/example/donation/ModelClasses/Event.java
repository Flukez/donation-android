package com.example.donation.ModelClasses;

public class Event {

    public String name;
    public String address;
    public String phonenumber;
    public String latitude;
    public String longitude;
    public String category;

    public Event(){

    }
    public Event(String name, String address, String phonenumber, String latitude, String longitude, String category) {
        this.name=name;
        this.address=address;
        this.phonenumber=phonenumber;
        this.latitude=latitude;
        this.longitude=longitude;
        this.category=category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
