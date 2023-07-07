package com.example.hotel_management_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_management_application.R;
import com.example.hotel_management_application.bookingapi.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ApproveBookingAdapter extends RecyclerView.Adapter<ApproveBookingAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<BookingModel> arrayList = new ArrayList<>();

    public ApproveBookingAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ApproveBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_approve_booking, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ApproveBookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, CustomerEmail, roomID, roomTitle, startDate, endDate, status, imageUrl;
        int bookingDays, price, totalPayment;

        roomTitle = arrayList.get(holder.getAdapterPosition()).getRoomTitle();
        id = arrayList.get(holder.getAdapterPosition()).getId();
        CustomerEmail = arrayList.get(holder.getAdapterPosition()).getCustomerEmail();
        startDate = arrayList.get(holder.getAdapterPosition()).getStartDate();
        status = arrayList.get(holder.getAdapterPosition()).getStatus();
        imageUrl = arrayList.get(holder.getAdapterPosition()).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();
        endDate = arrayList.get(holder.getAdapterPosition()).getEndDate();
        totalPayment = arrayList.get(holder.getAdapterPosition()).getTotalPayment();
        bookingDays = arrayList.get(holder.getAdapterPosition()).getBookingDays();

        //set view
        holder.edMail.setText(CustomerEmail);
        holder.edTitle.setText(roomTitle);
        holder.edStatus.setText("Status: " + status);
        holder.edStartDate.setText("Start Date: " + startDate);
        holder.edNights.setText("Nights: " + bookingDays);
        holder.edPrice.setText("Price: " + "$" + price);
        holder.edTotal.setText("Total: " + "$" + totalPayment);

        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);

        //cancel booking
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("BookingData").document(id);
                record.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(view.getContext(), "Booking Canceled", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //approve
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("BookingData").document(id);
                record.update("status", "accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "Booking Approved", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView edTitle;
        private final TextView edPrice;
        private final TextView edTotal;
        private final TextView edStatus;
        private final TextView edStartDate;
        private final TextView edNights;
        private final TextView edMail;
        private final ImageView imageView;
        private final Button cancel;
        private final Button approve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.apcardTitle);
            edPrice = itemView.findViewById(R.id.apcardPrice);
            imageView = itemView.findViewById(R.id.apcardImage);
            edNights = itemView.findViewById(R.id.apcardDays);
            edStartDate = itemView.findViewById(R.id.apcardStartDate);
            edStatus = itemView.findViewById(R.id.apcardStatus);
            edTotal = itemView.findViewById(R.id.apcardTotalPrice);
            edMail = itemView.findViewById(R.id.apcardEmail);
            cancel = itemView.findViewById(R.id.apcardCancel);
            approve = itemView.findViewById(R.id.apcardApprove);

        }
    }
}
