package com.example.hotel_management_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotel_management_application.R;
import com.example.hotel_management_application.bookingapi.BookingModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class AllBookingHistoryAdapter extends RecyclerView.Adapter<AllBookingHistoryAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<BookingModel> arrayList;

    public AllBookingHistoryAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AllBookingHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_all_booking_history, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AllBookingHistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, CustomerEmail,roomID, roomTitle, startDate, endDate,status, imageUrl;
        int bookingDays, price, totalPayment;

        roomTitle = arrayList.get(holder.getAdapterPosition()).getRoomTitle();
        id = arrayList.get(holder.getAdapterPosition()).getId();
        CustomerEmail = arrayList.get(holder.getAdapterPosition()).getCustomerEmail();
        startDate =arrayList.get(holder.getAdapterPosition()).getStartDate();
        status = arrayList.get(holder.getAdapterPosition()).getStatus();
        imageUrl = arrayList.get(holder.getAdapterPosition()).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();
        totalPayment = arrayList.get(holder.getAdapterPosition()).getTotalPayment();
        bookingDays = arrayList.get(holder.getAdapterPosition()).getBookingDays();
        endDate = arrayList.get(holder.getAdapterPosition()).getEndDate();

        //set view
        holder.endDate.setText(endDate);
        holder.edMail.setText(CustomerEmail);
        holder.edTitle.setText(roomTitle);
        holder.edStatus.setText("Status: " + status);
        holder.edStartDate.setText("Start Date: " + startDate);
        holder.edNights.setText("Nights: " + bookingDays);
        holder.edPrice.setText("Price: " + "$" + price);
        holder.edTotal.setText("Total: " + "$" + totalPayment);

        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView edTitle;
        private final TextView edPrice;
        private final TextView edTotal;
        private final TextView edStatus;
        private final TextView edStartDate;
        private final TextView edNights;
        private final TextView edMail;
        private final TextView endDate;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.hscardTitle);
            edPrice = itemView.findViewById(R.id.hscardPrice);
            imageView = itemView.findViewById(R.id.hscardImage);
            edNights = itemView.findViewById(R.id.hscardDays);
            edStartDate = itemView.findViewById(R.id.hscardStartDate);
            edStatus = itemView.findViewById(R.id.hscardStatus);
            edTotal = itemView.findViewById(R.id.hscardTotalPrice);
            endDate = itemView.findViewById(R.id.hsendDate);
            edMail = itemView.findViewById(R.id.hscardEmail);
        }
    }
}
