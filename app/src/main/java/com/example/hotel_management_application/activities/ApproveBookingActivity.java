package com.example.hotel_management_application.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotel_management_application.R;
import com.example.hotel_management_application.adapters.ApproveBookingAdapter;
import com.example.hotel_management_application.bookingapi.BookingFetchData;
import com.example.hotel_management_application.bookingapi.BookingModel;
import com.example.hotel_management_application.bookingapi.BookingViewFetchMessage;

import java.util.ArrayList;
import java.util.Objects;

public class ApproveBookingActivity extends AppCompatActivity implements BookingViewFetchMessage {
    private RecyclerView ListDataView;
    private ApproveBookingAdapter Adapter;
    ImageView menu;
    TextView title;

    ArrayList<BookingModel> roomModelArrayList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //This Line hides the action bar
        setContentView(R.layout.admin_list_view);
        title = findViewById(R.id.pageTitle);

        menu = findViewById(R.id.onMenu);
        title.setText("Requested Booking List");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApproveBookingActivity.this, AdminPanelActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        ListDataView = findViewById(R.id.AdminListView);

        BookingFetchData bookingFetchData = new BookingFetchData(this, this);

        RecyclerViewMethod();
        bookingFetchData.onSuccessUpdate(this);
    }

    public void RecyclerViewMethod() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ListDataView.setLayoutManager(manager);
        ListDataView.setItemAnimator(new DefaultItemAnimator());
        ListDataView.setHasFixedSize(true);
        Adapter = new ApproveBookingAdapter(this, roomModelArrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(BookingModel message) {
        if (message != null && message.getStatus().equals("requested")) {
            BookingModel roomModel = new BookingModel(message.getId(), message.getCustomerEmail(),
                    message.getRoomID(), message.getRoomTitle(), message.getStartDate(),
                    message.getEndDate(), message.getStatus(), message.getImageUrl(),
                    message.getBookingDays(), message.getPrice(), message.getTotalPayment());
            roomModelArrayList.add(roomModel);

        }
        Adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ApproveBookingActivity.this, AdminPanelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ApproveBookingActivity.this, message, Toast.LENGTH_LONG).show();

    }
}
