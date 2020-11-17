package com.example.yourhistory.model;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable {

    private String _id;
    private String name;
    private String lastName;
    private String birthDate;
    private String phoneNumber;
    private String nameUser;
    private String password;
    private String urlPhotoProfile;
    private boolean active;

    public User() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlPhotoProfile() {
        return urlPhotoProfile;
    }

    public void setUrlPhotoProfile(String urlPhotoProfile) {
        this.urlPhotoProfile = urlPhotoProfile;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nameUser='" + nameUser + '\'' +
                ", password='" + password + '\'' +
                ", urlPhotoProfile='" + urlPhotoProfile + '\'' +
                ", active=" + active +
                '}';
    }
}
