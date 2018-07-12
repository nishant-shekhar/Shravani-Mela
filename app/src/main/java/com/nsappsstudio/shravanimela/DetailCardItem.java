package com.nsappsstudio.shravanimela;

public class DetailCardItem {

    public String srNumber;
    public String optionalTitle;
    public String innerTitle;
    public String subTitle;
    public String description;
    public String shift;
    public String phone;
    public boolean title;
    public String mainTitle;
    public int type;

    public DetailCardItem(String srNumber, String optionalTitle, String innerTitle, String subTitle, String description, String shift, String phone, boolean title, String mainTitle, int type) {
        this.srNumber = srNumber;
        this.optionalTitle = optionalTitle;
        this.innerTitle = innerTitle;
        this.subTitle = subTitle;
        this.description = description;
        this.shift = shift;
        this.phone = phone;
        this.title = title;
        this.mainTitle = mainTitle;
        this.type = type;
    }

    public String getSrNumber() {
        return srNumber;
    }

    public String getOptionalTitle() {
        return optionalTitle;
    }

    public String getInnerTitle() {
        return innerTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getShift() {
        return shift;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isTitle() {
        return title;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public int getType() {
        return type;
    }
}
