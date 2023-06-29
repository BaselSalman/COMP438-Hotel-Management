package com.example.hotel_management_application.bookingapi;

public interface BookingViewFetchMessage {
    void onUpdateSuccess(BookingModel message);
    void onUpdateFailure(String message);
}
