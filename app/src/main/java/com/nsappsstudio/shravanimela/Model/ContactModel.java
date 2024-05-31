package com.nsappsstudio.shravanimela.Model;

public class ContactModel {
    public String nameEng;
    public String nameHindi;
    public String designationEng;
    public String designationHindi;
    public String contact;

    public ContactModel(){

    }

    public ContactModel(String nameEng, String nameHindi, String designationEng, String designationHindi, String contact) {
        this.nameEng = nameEng;
        this.nameHindi = nameHindi;
        this.designationEng = designationEng;
        this.designationHindi = designationHindi;
        this.contact = contact;
    }

    public String getDesignationHindi() {
        return designationHindi;
    }

    public String getDesignationEng() {
        return designationEng;
    }

    public String getNameEng() {
        return nameEng;
    }

    public String getNameHindi() {
        return nameHindi;
    }

    public String getContact() {
        return contact;
    }
}
