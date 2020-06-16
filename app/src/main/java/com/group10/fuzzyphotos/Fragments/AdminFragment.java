package com.group10.fuzzyphotos.Fragments;

import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.group10.fuzzyphotos.PuzzleUploader;
import com.group10.fuzzyphotos.BuildConfig;
import com.group10.fuzzyphotos.R;
import com.group10.fuzzyphotos.Utils.FileHelper;
import com.muddzdev.styleabletoast.StyleableToast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment which is used to take pictures and upload them to the server
 */
public class AdminFragment extends Fragment {

    private AdminViewModel mViewModel; // view model for admin

    // UI - Components
    Button btn_uploadpuzzle;
    ImageButton ibtn_getpicture;
    EditText et_solution;

    // Puzzle uploading
    ProgressDialog progressDialog;
    PuzzleUploader uploader;

    // Request codes (For Intent usage)
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_CROP = 2;

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(AdminViewModel.class);

        ibtn_getpicture = v.findViewById(R.id.ibtn_getpicture);
        ibtn_getpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraIntent(); // Start intent for capturing image
            }
        });

        et_solution = v.findViewById(R.id.et_solution);

        uploader = new PuzzleUploader(getContext());

        btn_uploadpuzzle = v.findViewById(R.id.btn_uploadpuzzle);
        btn_uploadpuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploader.canUpload(et_solution.getText().toString(), mViewModel.getImageUri(), new FileHelper.Callback() {
                    @Override
                    public void call() {
                        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        et_solution.startAnimation(shake);
                    }
                })) {
                    uploader.uploadPuzzle(et_solution.getText().toString(), mViewModel.getImageUri(), progressDialog, new FileHelper.Callback() { // upload puzzle
                        @Override
                        public void call() {
                            resetUI();
                        }
                    });
                }
            }
        });

        progressDialog = new ProgressDialog(getContext());

        if (mViewModel.getImageUri() != null) {
            ibtn_getpicture.setImageURI(mViewModel.getImageUri()); // Set cropped image as image button
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: // After capturing an image
                if (resultCode == RESULT_OK) {
                    startCropIntent(); // Immediately start intent for cropping image
                } else {
                    StyleableToast.makeText(getContext(), "Image capture cancelled",
                            Toast.LENGTH_SHORT, R.style.standardToast).show();
                }
                break;
            case REQUEST_IMAGE_CROP: // After finishing crop selection
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    mViewModel.setImageUri(result.getUri());
                    // Set cropped image as image button
                    ibtn_getpicture.setImageURI(mViewModel.getImageUri());

                } else {
                    // Could go back to camera or go back to main activity
                    // For camera: startCameraIntent();
                    // For activity: do nothing here
                }
                break;
        }
    }

    /**
     * creates temporary file for storing image and starts camera intent
     */
    private void startCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = FileHelper.createTemporaryFile(getContext());
            } catch (IOException ex) {
                // Error occurred while creating the File
                StyleableToast.makeText(getContext(), "Error: File creation failed",
                        Toast.LENGTH_SHORT, R.style.standardToast).show();
            }

            if (photoFile != null) {
                mViewModel.setImageUri(FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile)); // imageUri contains 'Uri' address to store the image
                // Here we tell intent that this imageUri is the place to store the image
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mViewModel.getImageUri());
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Takes temporary image and starts cropping intent
     */
    private void startCropIntent() {
        // start cropping activity for pre-acquired image saved on the device
        // Takes the 'Uri' address of the stored image
        Intent cropIntent = CropImage.activity(mViewModel.getImageUri())
                .setGuidelines(CropImageView.Guidelines.OFF)
                .setActivityTitle("Puzzle - Picture")
                //.setMinCropResultSize(1500,1500)
                //.setMaxCropResultSize(1500,1500)
                .setCropMenuCropButtonTitle("Select")
                .setAllowRotation(false)
                .setAllowFlipping(false)
                .setAllowCounterRotation(false)
                .setFixAspectRatio(true)
                .setAspectRatio(1000,1000)
                .getIntent(getContext());
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    /**
     * Remove uploaded puzzle from UI (resets cropped image and removes text entered in EditText)
     */
    private void resetUI() {
        mViewModel.setImageUri(null);
        et_solution.setText("");

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.camera_image,
                null);
        ibtn_getpicture.setImageDrawable(drawable);
    }
}
