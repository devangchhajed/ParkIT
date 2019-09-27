package com.codeencounter.nagarro.Adapters;

public class BookingHistoryRecords {


    String parkinglotid, appcheckin, checkin, checkout, cost;


    public void setAppcheckin(String appcheckin) {
        this.appcheckin = appcheckin;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCost() {
        return cost;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public void setParkinglotid(String parkinglotid) {
        this.parkinglotid = parkinglotid;
    }

    public String getAppcheckin() {
        return appcheckin;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public String getParkinglotid() {
        return parkinglotid;
    }


}
