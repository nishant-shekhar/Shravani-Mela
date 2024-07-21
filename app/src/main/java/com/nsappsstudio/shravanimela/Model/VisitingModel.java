package com.nsappsstudio.shravanimela.Model;

public class VisitingModel {
    public String name;
    public String street;
    public String city;
    public String state;
    public Long personCount;

    public VisitingModel(){

    }

    public VisitingModel(String name, String street, String city, String state, Long personCount) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.personCount = personCount;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Long getPersonCount() {
        return personCount;
    }
}
