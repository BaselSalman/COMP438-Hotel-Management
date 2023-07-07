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
import com.example.hotel_management_application.adapters.CustomerListAdapter;
import com.example.hotel_management_application.userapi.UserFetchData;
import com.example.hotel_management_application.userapi.UserModel;
import com.example.hotel_management_application.userapi.UserViewFetchMessage;

import java.util.ArrayList;
import java.util.Objects;

public class CustomerListActivity extends AppCompatActivity implements UserViewFetchMessage {
    private RecyclerView ListDataView;
    private CustomerListAdapter Adapter;
    ImageView menu;
    TextView title;

    ArrayList<UserModel> roomModelArrayList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.admin_list_view);
        title = findViewById(R.id.pageTitle);

        menu = findViewById(R.id.onMenu);
        title.setText("Customer List");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerListActivity.this, AdminPanelActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        ListDataView = findViewById(R.id.AdminListView);
        UserFetchData userFetchData = new UserFetchData(this, this);
        RecyclerViewMethod();
        userFetchData.onSuccessUpdate(this);
    }

    public void RecyclerViewMethod() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ListDataView.setLayoutManager(manager);
        ListDataView.setItemAnimator(new DefaultItemAnimator());
        ListDataView.setHasFixedSize(true);
        Adapter = new CustomerListAdapter(this, roomModelArrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(UserModel message) {
        if(message != null){
            UserModel userModel = new UserModel(message.getName(), message.getEmail(),
                    message.getPassword());
            roomModelArrayList.add(userModel);
        }
        Adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CustomerListActivity.this, AdminPanelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(CustomerListActivity.this, message, Toast.LENGTH_LONG).show();

    }
}