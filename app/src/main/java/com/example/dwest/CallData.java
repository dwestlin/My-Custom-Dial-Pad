package com.example.dwest;

public class CallData {
    private String number;
    private String date;
    private String latitude,longitude;

    public CallData(){

    }

    public CallData(String number, String date, String latitude, String longitude){
        this.number = number;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String pos) {
        this.latitude = pos;
    }

    public void setLongitude(String pos){this.longitude = pos;}

    public String getLongitude(){return longitude;}

}
