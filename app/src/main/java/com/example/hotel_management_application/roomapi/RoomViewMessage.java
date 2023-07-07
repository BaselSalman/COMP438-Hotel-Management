package com.example.hotel_management_application.roomapi;

public interface RoomViewMessage {

    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
