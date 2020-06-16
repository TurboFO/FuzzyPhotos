package com.group10.fuzzyphotos.Fragments;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for Admin activity. This ViewModel contains the imageUri variables which therefore
 * is saved after a screen rotation occurred
 */
public class AdminViewModel extends ViewModel {

    private Uri imageUri = null;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
