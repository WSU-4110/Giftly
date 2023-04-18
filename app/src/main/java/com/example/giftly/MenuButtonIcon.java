package com.example.giftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuButtonIcon extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ImageView imageview;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button button;
    private StorageReference mStorageReference;
    ConstraintLayout constraintLayout;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.menu.profile_menu);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("image/image.png");
        try {
            final File localFile = File.createTempFile("image","image");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MenuButtonIcon.this, "Picture Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.menu_item_profile)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MenuButtonIcon.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}