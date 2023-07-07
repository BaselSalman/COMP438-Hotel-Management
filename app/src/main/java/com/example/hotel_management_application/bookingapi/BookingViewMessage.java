package com.example.hotel_management_application.bookingapi;

public interface BookingViewMessage {
    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
