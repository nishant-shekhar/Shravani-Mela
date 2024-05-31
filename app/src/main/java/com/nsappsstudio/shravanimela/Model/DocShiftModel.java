package com.nsappsstudio.shravanimela.Model;

public class DocShiftModel {
    public String place;
    public String doc;
    public String docContact;
    public String staff;
    public DocShiftModel(){

    }
    public DocShiftModel(String place, String doc, String docContact, String staff) {
        this.place = place;
        this.doc = doc;
        this.docContact = docContact;
        this.staff = staff;
    }

    public String getPlace() {
        return place;
    }

    public String getDoc() {
        return doc;
    }

    public String getDocContact() {
        return docContact;
    }

    public String getStaff() {
        return staff;
    }
}
