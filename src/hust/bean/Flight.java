package hust.bean;

import java.sql.Date;
import java.sql.Time;

public class Flight {
    private String flightNo;
    private String takeoffCity;
    private String landingCity;
    private Time takeoffTime;
    private Time landingTime;
    private Time duration;
    private int FCRemain;
    private int BCRemain;
    private int ECRemain;
    private float FCPrice;
    private float BCPrice;
    private float ECPrice;

    public Flight() {
        this.flightNo = "MU2460";
        this.takeoffTime = new Time(8, 10, 0);
        this.landingTime = new Time(10, 25, 0);
        this.duration = new Time(2, 15,0);
        this.takeoffCity = "北京";
        this.landingCity = "武汉";
        this.FCRemain = 0;
        this.FCPrice = 10000.0f;
        this.BCRemain = 5;
        this.BCPrice = 2000.0f;
        this.ECRemain = 120;
        this.ECPrice = 1189.0f;
    }
    public Flight(String flightNo, String takeoffCity, String landingCity, Time takeoffTime, Time landingTime,
                  Time duration, int FCRemain, int BCRemain, int ECRemain, float FCPrice, float BCPrice, float ECPrice) {
        this.flightNo = flightNo;
        this.takeoffCity = takeoffCity;
        this.landingCity = landingCity;
        this.takeoffTime = takeoffTime;
        this.landingTime = landingTime;
        this.duration = duration;
        this.FCRemain = FCRemain;
        this.BCRemain = BCRemain;
        this.ECRemain = ECRemain;
        this.FCPrice = FCPrice;
        this.BCPrice = BCPrice;
        this.ECPrice = ECPrice;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public void setTakeoffTime(Time takeoffTime) {
        this.takeoffTime = takeoffTime;
    }

    public Time getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(Time landingTime) {
        this.landingTime = landingTime;
    }

    public void setTakeoffCity(String takeoffCity) {
        this.takeoffCity = takeoffCity;
    }

    public void setLandingCity(String landingCity) {
        this.landingCity = landingCity;
    }


    public void setFCRemain(int FCRemain) {
        this.FCRemain = FCRemain;
    }

    public void setBCRemain(int BCRemain) {
        this.BCRemain = BCRemain;
    }

    public void setECRemain(int ECRemain) {
        this.ECRemain = ECRemain;
    }

    public void setFCPrice(float FCPrice) {
        this.FCPrice = FCPrice;
    }

    public void setBCPrice(float BCPrice) {
        this.BCPrice = BCPrice;
    }

    public void setECPrice(float ECPrice) {
        this.ECPrice = ECPrice;
    }

    public String getTakeoffCity() {
        return takeoffCity;
    }

    public String getLandingCity() {
        return landingCity;
    }

    public Time getTakeoffTime() {
        return takeoffTime;
    }

    public int getFCRemain() {
        return FCRemain;
    }

    public int getBCRemain() {
        return BCRemain;
    }

    public int getECRemain() {
        return ECRemain;
    }

    public float getFCPrice() {
        return FCPrice;
    }

    public float getBCPrice() {
        return BCPrice;
    }

    public float getECPrice() {
        return ECPrice;
    }
}
