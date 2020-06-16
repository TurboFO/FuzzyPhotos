package com.group10.fuzzyphotos;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group10.fuzzyphotos.BuildConfig;
import com.group10.fuzzyphotos.Firebase.PuzzleEntry;
import com.group10.fuzzyphotos.Firebase.PuzzleEntryInfo;
import com.group10.fuzzyphotos.Fragments.PuzzleViewModel;
import com.group10.fuzzyphotos.Utils.Constants;
import com.group10.fuzzyphotos.Utils.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * class which retrieves the images + answers from the database
 */
public class PuzzleRetriever {

    // Context of activity using this class
    Context context;
    PuzzleViewModel viewModel;

    // Firebase
    FirebaseAuth mFirebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    public PuzzleRetriever(Context activity_context, PuzzleViewModel view_model) {
        context = activity_context;
        viewModel = view_model;

        // Setup firebase references
        mFirebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Database_Path);
    }

    /**
     * Retrieves image-solution combination (puzzle) from Firebase
     * It retrieves only not previously encountered entries (based on 'encounteredPuzzles')
     * And the only time it retrieves a 'passed_key' is when it is the only un-encountered entry
     * left
     */
    public void retrieveNewPuzzle(final String passed_key, final FileHelper.Callback showfinish_callback, final FileHelper.Callback retrieved_callback ){

        viewModel.blurMode = 1; // Reset blurMode
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> entries = new ArrayList<>(); // List of all database entries
                // Retrieve all entries from database
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    entries.add(ds.getKey()); // Add entry-key to list
                }

                // User has played all puzzles
                if (viewModel.encounteredPuzzles.size() >= entries.size()) {
                    showfinish_callback.call();
                    return;
                }

                Random ran = new Random();
                int chosen_entry;
                String chosen_key;
                do {
                    chosen_entry = ran.nextInt(entries.size()); // Pick one random entry
                    chosen_key = entries.get(chosen_entry);
                }
                while(viewModel.encounteredPuzzles.contains(chosen_key) ||
                        (chosen_key.equals(passed_key) &&
                                viewModel.encounteredPuzzles.size() < entries.size() - 1));
                /**
                 * Keep looking for keys while:
                 * - You have already encountered the chosen key
                 * - Or the chosen key is the passed key while there are more than 1 entries
                 * left to choose from
                 */

                viewModel.currentPuzzleKey = chosen_key;
                final PuzzleEntryInfo retrieved_entry = dataSnapshot.child(chosen_key)
                        .getValue(PuzzleEntryInfo.class);
                // Update list of encountered puzzles
                viewModel.encounteredPuzzles.add(viewModel.currentPuzzleKey);

                // Use database entry to create reference to actual image
                StorageReference referenceImage = FirebaseStorage.getInstance().
                        getReference(Constants.Storage_Path +
                                retrieved_entry.getPuzzleImageRef());

                // Max image size downloadable from firebase storage
                final long FIVE_MEGABYTE = 1024 * 1024 * 5;
                // Get actual image from firebase storage
                referenceImage.getBytes(FIVE_MEGABYTE)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                File f = null;
                                try {
                                    f = FileHelper.createTemporaryFile(context);
                                    FileOutputStream fo = new FileOutputStream(f);
                                    fo.write(bytes);
                                    fo.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Uri image_uri  = FileProvider.getUriForFile(context,
                                        BuildConfig.APPLICATION_ID+".provider",
                                        f); // imageUri contains 'Uri' address to store the image

                                viewModel.retrievedEntry =
                                        new PuzzleEntry(retrieved_entry.getPuzzleAnswer(), image_uri);

                                retrieved_callback.call();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
