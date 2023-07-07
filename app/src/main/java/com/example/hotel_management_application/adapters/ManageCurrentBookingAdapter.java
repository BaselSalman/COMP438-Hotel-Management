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

import com.example.hotel_management_application.LocalDateTimeFormatter;
import com.example.hotel_management_application.R;
import com.example.hotel_management_application.bookingapi.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;


public class ManageCurrentBookingAdapter extends RecyclerView.Adapter<ManageCurrentBookingAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<BookingModel> arrayList;

    public ManageCurrentBookingAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ManageCurrentBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_manage_current_booking, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ManageCurrentBookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, customerEmail, roomID, roomTitle, startDate, endDate, status, imageUrl;
        int bookingDays, price, totalPayment;

        roomTitle = arrayList.get(holder.getAdapterPosition()).getRoomTitle();
        id = arrayList.get(holder.getAdapterPosition()).getId();
        customerEmail = arrayList.get(holder.getAdapterPosition()).getCustomerEmail();
        startDate = arrayList.get(holder.getAdapterPosition()).getStartDate();
        status = arrayList.get(holder.getAdapterPosition()).getStatus();
        imageUrl = arrayList.get(holder.getAdapterPosition()).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();
        totalPayment = arrayList.get(holder.getAdapterPosition()).getTotalPayment();
        bookingDays = arrayList.get(holder.getAdapterPosition()).getBookingDays();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        endDate = timestamp.toString();

        //set view
        holder.edMail.setText(customerEmail);
        holder.edTitle.setText(roomTitle);
        holder.edStatus.setText("Status: " + status);
        holder.edStartDate.setText("Start Date: " + startDate);
        holder.edNights.setText("Nights: " + bookingDays);
        holder.edPrice.setText("Price: " + "$" + price);
        holder.edTotal.setText("Total: " + "$" + totalPayment);
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);

        //approve
        holder.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("BookingData").document(id);

                record.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String startDateString = (String) documentSnapshot.get("startDate");
                        int bookingDays = Integer.parseInt(documentSnapshot.get("bookingDays").toString());
                        LocalDate date = LocalDateTimeFormatter.makeDateFromString(startDateString);
                        if (LocalDate.now().isBefore(date.plusDays(bookingDays))) {
                            Toast.makeText(view.getContext(), "Can't check out before the end date", Toast.LENGTH_LONG).show();
                        } else {
                            record.update("status", "checkedOut", "endDate", endDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(), "Booking Checked out", Toast.LENGTH_LONG).show();
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
        private final Button checkout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.mcardTitle);
            edPrice = itemView.findViewById(R.id.mcardPrice);
            imageView = itemView.findViewById(R.id.mcardImage);
            edNights = itemView.findViewById(R.id.mcardDays);
            edStartDate = itemView.findViewById(R.id.mcardStartDate);
            edStatus = itemView.findViewById(R.id.mcardStatus);
            edTotal = itemView.findViewById(R.id.mcardTotalPrice);
            edMail = itemView.findViewById(R.id.mcardEmail);
            checkout = itemView.findViewById(R.id.mcardCheckout);
        }
    }
}
