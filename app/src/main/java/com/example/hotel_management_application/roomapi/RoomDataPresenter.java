package com.example.hotel_management_application.roomapi;

import android.app.Activity;

public interface RoomDataPresenter {
    void onSuccessUpdate(Activity activity, String id, String title, String description, String isAvailable, String location, String imageUrl, int price);

}
