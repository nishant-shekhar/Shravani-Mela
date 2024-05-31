package com.nsappsstudio.shravanimela.Model;

public class PlacesModel {
    public String name;
    public String type;
    public Double lat;
    public Double lang;
    public String address;
    public String contact;
    public String distance;
    public String picUrl;
    public boolean selected;
    public PlacesModel(){

    }

    public PlacesModel(String name, String type, Double lat, Double lang, String address, String contact, String distance, String picUrl) {
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.contact = contact;
        this.distance = distance;
        this.picUrl = picUrl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLang() {
        return lang;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getDistance() {
        return distance;
    }
}
