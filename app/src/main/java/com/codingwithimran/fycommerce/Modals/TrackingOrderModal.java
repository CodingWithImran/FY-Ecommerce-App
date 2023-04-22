package com.codingwithimran.fycommerce.Modals;

public class TrackingOrderModal {
    String currentDate, currentTime, TrackingStatus, customerId;

    public TrackingOrderModal() {
    }

    public TrackingOrderModal(String currentDate, String currentTime, String trackingStatus, String customerId) {
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        TrackingStatus = trackingStatus;
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTrackingStatus() {
        return TrackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        TrackingStatus = trackingStatus;
    }
}
