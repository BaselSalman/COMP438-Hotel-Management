package com.example.hotel_management_application.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotel_management_application.R;
import com.example.hotel_management_application.adapters.ManageRoomsAdapter;
import com.example.hotel_management_application.roomapi.RoomFetchData;
import com.example.hotel_management_application.roomapi.RoomModel;
import com.example.hotel_management_application.roomapi.RoomViewFetchMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import javax.annotation.Nullable;

public class AdminManageRoomActivity extends Activity implements RoomViewFetchMessage {

    private RecyclerView ListDataView;
    private ManageRoomsAdapter manageRoomsAdapter;
    ArrayList<RoomModel> roomModelArrayList = new ArrayList<>();
    public Uri imageUri;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_activity);

        ListDataView = findViewById(R.id.ListViewRoom);
        TextView title = findViewById(R.id.pageTitle);
        title.setText("Manage Room Record");
        RoomFetchData roomFetchData = new RoomFetchData(this, this);
        RecyclerViewMethods();
        roomFetchData.onSuccessUpdate(this);
    }

    public void RecyclerViewMethods() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ListDataView.setLayoutManager(manager);
        ListDataView.setHasFixedSize(true);
        manageRoomsAdapter = new ManageRoomsAdapter(this, roomModelArrayList, this, ListDataView);
        ListDataView.setAdapter(manageRoomsAdapter);
        ListDataView.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= 0 && requestCode < roomModelArrayList.size() && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            manageRoomsAdapter.uploadImage(requestCode, imageUri);
        }
    }

    private void uploadFile(){
        if(imageUri != null){
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading the image...");
            pd.show();
            StorageReference fileReference = storageReference.child(imageUri.getLastPathSegment());
            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    String sImageUri = uri.toString();
                                    Toast.makeText(AdminManageRoomActivity.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminManageRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new com.google.firebase.storage.OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            pd.setMessage("Progress: "+ (int) progress + "%");
                        }
                    });


        }else{
            Toast.makeText(this, "No Image selected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpdateSuccess(RoomModel message) {
        if(message != null){
            RoomModel roomModel = new RoomModel(message.getId(),message.getTitle(),message.getDescription(),message.getIsAvailable(),message.getLocation(),
                    message.getImageUrl(),message.getPrice());

            roomModelArrayList.add(roomModel);
        }
        manageRoomsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(AdminManageRoomActivity.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminManageRoomActivity.this, AdminPanelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
