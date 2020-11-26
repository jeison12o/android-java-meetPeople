package com.example.yourhistory.model;

import java.io.Serializable;
import java.util.Date;

public class Multimedia implements Serializable {

    private String _id;
    private String _iduserTo;
    private String type;
    private String url;
    private String dateCreation;
    private String category;

    public Multimedia() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_iduserTo() {
        return _iduserTo;
    }

    public void set_iduserTo(String _iduserTo) {
        this._iduserTo = _iduserTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
