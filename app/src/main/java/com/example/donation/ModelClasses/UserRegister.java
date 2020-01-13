package com.example.donation.ModelClasses;

public class UserRegister {

//    String userId;
    String email;
    String password;
    String firstname;
    String lastname;
    String phone;
    String address;
    String type;

    public UserRegister() {

    }

    public UserRegister(String userId, String email, String password, String firstname, String lastname, String phone, String address, String type) {


        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.address = address;
        this.type = type;
    }

    public UserRegister(String id, String addemail, String addpassword, String addfirsname, String addlastname, String addphone, String addaddress) {
    }

//    public String getUserId() {
//        return userId;
//    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }
}