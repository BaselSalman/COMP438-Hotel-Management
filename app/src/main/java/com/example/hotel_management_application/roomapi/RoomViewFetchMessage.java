package com.example.hotel_management_application.roomapi;

public interface RoomViewFetchMessage {
    void onUpdateSuccess(RoomModel message);
    void onUpdateFailure(String message);

}
