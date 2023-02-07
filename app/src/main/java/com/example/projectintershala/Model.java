package com.example.projectintershala;

public class Model {

    String IMEIDevice,InternetConnectivity,BatteryCharging,BatteryPercentage,Location,Timestamp,picture, User;

    public Model(String firstImei, String secoundIcs, String thirdBcs, String fourthBc, String fivethL, String sixthTS, String User) {
    }

    public Model(String IMEIDevice, String internetConnectivity, String batteryCharging, String batteryPercentage, String location, String timestamp, String picture, String User) {
        this.IMEIDevice = IMEIDevice;
        InternetConnectivity = internetConnectivity;
        BatteryCharging = batteryCharging;
        BatteryPercentage = batteryPercentage;
        Location = location;
        Timestamp = timestamp;
        this.picture = picture;
        this.User = User;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getIMEIDevice() {
        return IMEIDevice;
    }

    public void setIMEIDevice(String IMEIDevice) {
        this.IMEIDevice = IMEIDevice;
    }

    public String getInternetConnectivity() {
        return InternetConnectivity;
    }

    public void setInternetConnectivity(String internetConnectivity) {
        InternetConnectivity = internetConnectivity;
    }

    public String getBatteryCharging() {
        return BatteryCharging;
    }

    public void setBatteryCharging(String batteryCharging) {
        BatteryCharging = batteryCharging;
    }

    public String getBatteryPercentage() {
        return BatteryPercentage;
    }

    public void setBatteryPercentage(String batteryPercentage) {
        BatteryPercentage = batteryPercentage;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
