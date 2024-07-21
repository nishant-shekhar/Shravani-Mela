package com.nsappsstudio.shravanimela.Model;

import java.util.List;

public class AmbulanceModel {
    public String vehicleNo;
    public String place;
    public String shift;
    public List<ContactModel> contactModels;
    public AmbulanceModel(){

    }
    public AmbulanceModel(String vehicleNo, String place, String shift,List<ContactModel> contactModels) {
        this.vehicleNo = vehicleNo;
        this.place = place;
        this.shift = shift;
        this.contactModels = contactModels;
    }

    public String getShift() {
        return shift;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getPlace() {
        return place;
    }

    public List<ContactModel> getContactModels() {
        return contactModels;
    }
}
