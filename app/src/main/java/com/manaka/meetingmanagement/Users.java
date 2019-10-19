package com.manaka.meetingmanagement;

public class Users {
    String Name;
    String Phone;
    String Event;
    String Place;



    public Users(){

    }
    public Users(String Name, String Phone, String Event, String Place) {
        this.Name = Name;
        this.Phone = Phone;
        this.Event = Event;
        this.Place = Place;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String Event) {
        this.Event = Event;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String Place) {
        this.Place = Place;
    }

}
