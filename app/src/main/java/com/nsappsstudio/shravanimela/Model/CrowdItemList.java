package com.nsappsstudio.shravanimela.Model;

public class CrowdItemList {
    public String lastUpdate;
    public String place;
    public String crowdLevel;

    public CrowdItemList(String lastUpadte, String place, String crowdLevel) {
        this.lastUpdate = lastUpadte;
        this.place = place;
        this.crowdLevel = crowdLevel;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getPlace() {
        return place;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }
}
