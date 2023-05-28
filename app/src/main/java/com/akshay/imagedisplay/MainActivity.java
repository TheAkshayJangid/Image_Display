package com.akshay.imagedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final int RESULT_LOAD_IMG =100;
    ImageView img;
    Button Select;
    private StorageReference storageReference;
    private Uri selectedImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.action_image);
        Select = findViewById(R.id.ImgSelect);
        storageReference = FirebaseStorage.getInstance().getReference();
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
            uploadImage();
        }
    }
    private void uploadImage() {
        if (selectedImageUri != null) {
            StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID().toString());
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }


//    @Override protected void onActivityResult(int reqCode, int resultCode, Intent data) { super.onActivityResult(reqCode, resultCode, data);
//        if (resultCode == RESULT_OK) { try { final Uri imageUri = data.getData();
//            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//            img.setImageBitmap(selectedImage);
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//        } }
//        else { Toast.makeText(MainActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
//        } }
    }