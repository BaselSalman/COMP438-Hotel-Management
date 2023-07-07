package com.example.hotel_management_application.userapi;

public interface UserViewFetchMessage {
    void onUpdateSuccess(UserModel message);
    void onUpdateFailure(String message);
}
