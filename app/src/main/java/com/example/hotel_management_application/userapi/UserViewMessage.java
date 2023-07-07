package com.example.hotel_management_application.userapi;

public interface UserViewMessage {
    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
