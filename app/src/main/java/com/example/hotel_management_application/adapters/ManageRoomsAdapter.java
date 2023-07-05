package com.example.hotel_management_application.adapters;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotel_management_application.R;
import com.example.hotel_management_application.activities.AddRoomActivity;
import com.example.hotel_management_application.activities.AdminManageRoomActivity;
import com.example.hotel_management_application.roomapi.RoomModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageRoomsAdapter extends RecyclerView.Adapter<ManageRoomsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RoomModel> arrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private AdminManageRoomActivity activity;
    public Uri uri;
    private RecyclerView recyclerView;
    private String url;

    private StorageReference storageReference;
    private StorageTask uploadtask;

    public ManageRoomsAdapter(Context context, ArrayList<RoomModel> arrayList,
                              AdminManageRoomActivity activity, RecyclerView recyclerView) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.storageReference = FirebaseStorage.getInstance().getReference("RoomImages");
    }

    @NonNull
    @Override
    public ManageRoomsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_room_card, parent, false);
        ManageRoomsAdapter.ViewHolder holder = new ManageRoomsAdapter.ViewHolder(view);
        return holder;
    }

    private void selectImage(int position) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        activity.startActivityForResult(intent,100);
        activity.startActivityForResult(intent,position);
    }

    public void uploadImage(int position, Uri imageUri) {
        ManageRoomsAdapter.ViewHolder viewHolder = (ManageRoomsAdapter.ViewHolder)
                recyclerView.findViewHolderForAdapterPosition(position);
        Picasso.with(activity).load(imageUri).fit().into(viewHolder.imageView);
        uri = imageUri;
        url = imageUri.toString();
    }


    @Override
    public void onBindViewHolder(@NonNull ManageRoomsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String title, description, location, imageUrl;
        int price;

        title = arrayList.get(holder.getAdapterPosition()).getTitle();
        description =arrayList.get(holder.getAdapterPosition()).getDescription();
        location = arrayList.get(holder.getAdapterPosition()).getLocation();
        imageUrl = arrayList.get(position).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();

        holder.edTitle.setText(title);
        holder.edDesc.setText(description);
        holder.edLocation.setText(location);
        holder.edPrice.setText("$" + String.valueOf(price));
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);
        String id= arrayList.get(holder.getAdapterPosition()).getId();
        //delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("RoomData").document(id);
                record.delete().addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(v.getContext(), "Room Deleted", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(AdminManageRoomActivity.class,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//                } else {
                    selectImage(holder.getAdapterPosition());
//                }
            }
        });


        //update
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(url != null)
                    uploadFile(holder, view, id);
                else updateData(holder, view, id);
            }
        });
    }

    private void uploadFile(ViewHolder holder, View view, String id){
        if(url != null){
//            Log.d(TAG, "uploadfile: getLastPathSegment type " + imageUri.getLastPathSegment());
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setTitle("Uploading the image...");
            pd.show();
            StorageReference fileReference = storageReference.child(uri.getLastPathSegment());
            uploadtask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    url = uri.toString();
//                                    Log.d(TAG, "uploadFile: url will be upload " + sImageUri);
                                    Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    updateData(holder, view, id);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new com.google.firebase.storage.OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            pd.setMessage("Progress: "+ (int) progress + "%");
                        }
                    });
        }else{
            Toast.makeText(context, "No Image Selected", Toast.LENGTH_LONG).show();
        }
    }

    public void updateData(ViewHolder holder, View view, String id) {
        String title = holder.edTitle.getText().toString();
        String description =holder.edDesc.getText().toString();
        String location = holder.edLocation.getText().toString();

        int price = Integer.parseInt(holder.edPrice.getText().toString());

        if(!TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(description)
                && !TextUtils.isEmpty(location)){

            firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference record = firebaseFirestore.collection("RoomData").document(id);
            record.update("title",title, "description",description,
                    "location", location, "price", price, "imageUrl", url).addOnSuccessListener(new OnSuccessListener< Void >() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(view.getContext(), "Room Updated", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            if(TextUtils.isEmpty(title)){
                holder.edTitle.setError("Title is required");
                return;
            }if (TextUtils.isEmpty(description)){
                holder.edDesc.setError("Description is required");
                return;
            }
            if (TextUtils.isEmpty(location)){
                holder.edLocation.setError("Location is required");
                return;
            }
            if (holder.edPrice.getText() == null){
                holder.edPrice.setError("Price is required");
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText edTitle, edDesc, edLocation, edPrice;
        private Button delete, update;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.vcardTitle);
            edDesc = itemView.findViewById(R.id.vcardDescription);
            edLocation = itemView.findViewById(R.id.vcardLocation);
            edPrice = itemView.findViewById(R.id.vcardPrice);
            update = itemView.findViewById(R.id.vcardUpdate);
            delete = itemView.findViewById(R.id.vcardDelete);
            imageView = itemView.findViewById(R.id.vcardImage);
        }
    }
}