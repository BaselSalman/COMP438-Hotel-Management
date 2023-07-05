package com.example.hotel_management_application.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotel_management_application.R;
import com.example.hotel_management_application.adapters.BestOfferAdapter;
import com.example.hotel_management_application.roomapi.RoomFetchData;
import com.example.hotel_management_application.roomapi.RoomModel;
import com.example.hotel_management_application.roomapi.RoomViewFetchMessage;

import java.util.ArrayList;
import java.util.Objects;

public class ViewAllRoomsActivity extends AppCompatActivity implements RoomViewFetchMessage {
    private RecyclerView ListDataView;
    private BestOfferAdapter Adapter;
    ImageView menu, profile;
    TextView title;

    ArrayList<RoomModel> roomModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_view_all);
        title = findViewById(R.id.pageTitle);

        menu = findViewById(R.id.onMenu);
        profile= findViewById(R.id.onProfile);
        title.setText("All the available rooms");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllRoomsActivity.this, UserMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllRoomsActivity.this, UserProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        ListDataView = findViewById(R.id.AllListView);

        RoomFetchData roomFetchData = new RoomFetchData(this, this);

        RecyclerViewMethod();
        roomFetchData.onSuccessUpdate(this);

    }
    public void RecyclerViewMethod() {

//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        ListDataView.setLayoutManager(manager);
//        ListDataView.setItemAnimator(new DefaultItemAnimator());
//        ListDataView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        ListDataView.setLayoutManager(mLayoutManager);

        Adapter = new BestOfferAdapter(this, roomModelArrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(RoomModel message) {
        if(message != null){
            RoomModel roomModel = new RoomModel(message.getId(),message.getTitle(),message.getDescription(),message.getIsAvailable(),
                    message.getLocation(), message.getImageUrl(), message.getPrice());
            roomModelArrayList.add(roomModel);

        }
        Adapter.notifyDataSetChanged();
    }

    public void onProfilePage(View view) {
        Intent intent = new Intent(ViewAllRoomsActivity.this, UserProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onMenuClick(View view) {
        Intent intent = new Intent(ViewAllRoomsActivity.this, UserMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewAllRoomsActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ViewAllRoomsActivity.this, message, Toast.LENGTH_LONG).show();

    }
}