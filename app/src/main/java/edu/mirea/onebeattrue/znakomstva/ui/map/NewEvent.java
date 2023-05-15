package edu.mirea.onebeattrue.znakomstva.ui.map;

public class NewEvent {
    private String eventId;
    private String eventUser;
    private String eventName;
    private String eventDescription;
    private String eventTime;
    private String eventPlace;
    private String eventCategory;

    NewEvent() {}

    public NewEvent(String eventName, String eventDescription, String eventTime, String eventPlace) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventTime = eventTime;
        this.eventPlace = eventPlace;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventUser() {
        return eventUser;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }
}
