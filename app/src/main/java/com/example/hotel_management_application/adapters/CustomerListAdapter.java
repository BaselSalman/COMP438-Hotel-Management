package com.example.hotel_management_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_management_application.R;
import com.example.hotel_management_application.userapi.UserModel;

import java.util.ArrayList;


public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<UserModel> arrayList;
    int index = 0;

    public CustomerListAdapter(Context context, ArrayList<UserModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_customer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name, email;
        name = arrayList.get(holder.getAdapterPosition()).getName();
        email = arrayList.get(holder.getAdapterPosition()).getEmail();

        //set view
        holder.edName.setText(name);
        holder.edEmail.setText(email);
        int backgroundColor;
        if (index % 2 == 0)
            backgroundColor = R.color.blue;
        else
            backgroundColor = R.color.light_green;
        index++;
        holder.cardImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, backgroundColor)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView edName, edEmail;
        private final ImageView cardImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edEmail = itemView.findViewById(R.id.ccardEmail);
            edName = itemView.findViewById(R.id.cndName);
            cardImage = itemView.findViewById(R.id.hscardImage);
        }
    }
}