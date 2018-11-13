package com.devjk.devtalk.controller;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageController {

    private static StorageController instance;
    private FirebaseStorage firebaseStorage;

    public StorageController(){
        //constructor
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public static StorageController getInstance() {
        if(instance == null){
            instance = new StorageController();
        }
        return instance;
    }

    public UploadTask uploadProfileUri(Uri profileUri, String uid){
        StorageReference profileRef = firebaseStorage.getReference()
                .child("Users/Profiles/"+uid);
        return profileRef.putFile(profileUri);
    }
    public Task<Uri> getDownloadURL(Uri profileUri, String uid){
        StorageReference profileRef = firebaseStorage.getReference()
                .child("Users/Profiles/"+uid);
        return profileRef.getDownloadUrl();
    }

}