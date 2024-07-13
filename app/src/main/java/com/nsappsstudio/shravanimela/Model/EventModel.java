package com.nsappsstudio.shravanimela.Model;

public class EventModel {
    public String eventId;
    public String eventName;
    public String eventDate;
    public String eventTime;
    public String details;
    public String dpUrl;
    public float stars;
    public long ts;

    public EventModel(String eventName, String eventDate, String eventTime, String details, String dpUrl, float stars, long ts) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.details = details;
        this.dpUrl = dpUrl;
        this.stars = stars;
        this.ts = ts;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getDetails() {
        return details;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public float getStars() {
        return stars;
    }

    public long getTs() {
        return ts;
    }
}
