package com.nsappsstudio.shravanimela.Model;

import java.util.List;

public class AmbulanceModel {
    public String vehicleNo;
    public String place;
    public List<ContactModel> contactModels;
    public AmbulanceModel(){

    }
    public AmbulanceModel(String vehicleNo, String place, List<ContactModel> contactModels) {
        this.vehicleNo = vehicleNo;
        this.place = place;
        this.contactModels = contactModels;
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
