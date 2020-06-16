package com.group10.fuzzyphotos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group10.fuzzyphotos.Firebase.PuzzleEntryInfo;
import com.group10.fuzzyphotos.R;
import com.group10.fuzzyphotos.Utils.Constants;
import com.group10.fuzzyphotos.Utils.FileHelper;
import com.muddzdev.styleabletoast.StyleableToast;

/**
 * class used to upload the taken pictures + the given answers to the database and gives connects
 * both the image and answer with an ID.
 */
public class PuzzleUploader {

    Context context;

    // Firebase
    StorageReference storageReference;
    DatabaseReference databaseReference;

    public PuzzleUploader(Context activity_context) {
        context = activity_context;

        // Setup firebase references
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Database_Path);
    }

    /**
     * Returns true if a solution text is filled in and an image is supplied. Otherwise false is returned
     * and feedback is given
     */
    public boolean canUpload(String solution, Uri image, FileHelper.Callback shake_callback) {
        // In case no solution and image provided
        if (solution.isEmpty() && image == null) {

            shake_callback.call(); // shake effect callback
            StyleableToast.makeText(context, "No image captured and solution filled in",
                    Toast.LENGTH_SHORT, R.style.standardToast).show();

        } else if (solution.isEmpty()) { // In case only no solution provided

            shake_callback.call(); // shake effect callback
            StyleableToast.makeText(context, "No solution filled in",
                    Toast.LENGTH_SHORT, R.style.standardToast).show();

        } else if (image  == null) { // In case only no image provided

            StyleableToast.makeText(context, "No image captured",
                    Toast.LENGTH_SHORT, R.style.standardToast).show();

        } else { // We have an image and solution
            return true;
        }
        return false;
    }

    /**
     * Uploads puzzle entry to the database ('solution', 'image'). The ProgressDialog and callback serve for ui purposes
     * which could not be handled in AdminFragment due to uploading being a seperate thread
     */
    public void uploadPuzzle(final String solution, final Uri image, final ProgressDialog pd, final FileHelper.Callback resetUI_callback) {
        final String imagePath;

        pd.setTitle("Puzzle is Uploading..."); // set progressDialog title
        pd.show(); // make it visible
        // Creating second StorageReference: This is the path to the image file e.g.
        // 'Storage_Path/1584354201071.jpg'
        imagePath = System.currentTimeMillis() + "." +
                FileHelper.GetFileExtension(image, context);
        StorageReference storageReference2nd = storageReference.child(Constants
                .Storage_Path + imagePath);

        // Actually fill path with the image file
        storageReference2nd.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // get puzzle_solution from EditText;
                        String puzzle_solution = solution.trim().toLowerCase();
                        // Hiding the progressDialog after done uploading
                        pd.dismiss();

                        // Showing toast message after done uploading.
                        StyleableToast.makeText(context, "Image Uploaded Successfully",
                                Toast.LENGTH_LONG, R.style.standardToast).show();

                        @SuppressWarnings("VisibleForTests")
                        PuzzleEntryInfo puzzle_entry = new PuzzleEntryInfo(puzzle_solution,
                                imagePath); // Create database entry

                        // Getting image upload ID.
                        String ImageUploadId = databaseReference.push().getKey();
                        //: creates a reference to an auto-generated child location
                        // and generates a child key
                        // .push()
                        // .getKey() : retrieves this generated key

                        // Adding image upload id s child element into databaseReference.
                        databaseReference.child(ImageUploadId).setValue(puzzle_entry);
                        //: retrieves a reference to the location relative to databaseReference
                        // .child(ImageUploadID)
                        // : sets the imageUploadInfo object at the given location
                        // .setValue(puzzle_entry)
                        resetUI_callback.call();
                    }
                })
                // If something goes wrong
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        pd.dismiss(); // Hiding the progressDialog

                        // Showing exception error message.
                        StyleableToast.makeText(context, exception.getMessage(),
                                Toast.LENGTH_LONG, R.style.standardToast).show();
                    }
                })

                // On progress change upload time
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        // Setting progressDialog Title
                        pd.setTitle("Puzzle is Uploading...");

                    }
                });
    }
}
