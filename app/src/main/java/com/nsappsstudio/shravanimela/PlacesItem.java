package com.nsappsstudio.shravanimela;

public class PlacesItem {
    public String title;
    public String subtitle;
    public String itemType;
    public int rank;
    public String additionalInfo;
    public double lat;
    public double lang;
    public String contact;
    public String address;

    public PlacesItem(String title, String subtitle, String itemType, int rank, String additionalInfo, double lat, double lang, String contact, String address) {
        this.title = title;
        this.subtitle = subtitle;
        this.itemType = itemType;
        this.rank = rank;
        this.additionalInfo = additionalInfo;
        this.lat = lat;
        this.lang = lang;
        this.contact = contact;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getItemType() {
        return itemType;
    }

    public int getRank() {
        return rank;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public double getLat() {
        return lat;
    }

    public double getLang() {
        return lang;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }
}
