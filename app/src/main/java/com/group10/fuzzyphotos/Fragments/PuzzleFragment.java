package com.group10.fuzzyphotos.Fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.group10.fuzzyphotos.PuzzleHint;
import com.group10.fuzzyphotos.PuzzleRetriever;
import com.group10.fuzzyphotos.R;
import com.group10.fuzzyphotos.Utils.BlurBuilder;
import com.group10.fuzzyphotos.Utils.FileHelper;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;


public class PuzzleFragment extends Fragment {

    private PuzzleViewModel mViewModel;

    // UI - Components
    ProgressBar pb_picture_retrieval;
    ConstraintLayout cl_finished;
    Button btn_submit;
    Button btn_passpuzzle;
    ImageView iv_picture;
    EditText et_answer;
    TextView tv_hint_stripes;
    PuzzleHint puzzleHint;

    // Puzzle retrieval from firebase
    PuzzleRetriever retriever;

    public static PuzzleFragment newInstance() {
        return new PuzzleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.puzzle_fragment, container, false);

        /*
        On first call of onCreate() it creates a new ViewModel instance.
        When this method is called again, it will return the pre-existing ViewModel.
        This is what preserves the data.
         */
        // Setup viewModel
        mViewModel = ViewModelProviders.of(this).get(PuzzleViewModel.class);
        puzzleHint = new PuzzleHint(mViewModel);
        retriever = new PuzzleRetriever(getContext(), mViewModel);

        pb_picture_retrieval = v.findViewById(R.id.pb_picture_retrieval);
        cl_finished = v.findViewById(R.id.cl_finished);
        iv_picture = v.findViewById(R.id.iv_picture);
        et_answer = v.findViewById(R.id.et_answer);
        tv_hint_stripes = v.findViewById(R.id.solutionLength);
        et_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_answer.getCurrentTextColor() != Color.BLACK) {
                    et_answer.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_submit = v.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only enable submit button when we are not loading/retrieving a puzzle
                if (pb_picture_retrieval.getVisibility() == View.INVISIBLE) {
                    // trim() to remove spaces in front and in back ; toLowerCase()
                    // to bring everything to lowercase
                    String filled_in_solution = et_answer.getText().toString().trim().toLowerCase();
                    // Answer correct
                    if (filled_in_solution.equals(mViewModel.retrievedEntry.getPuzzleAnswer())) {
                        StyleableToast.makeText(getContext(), "Correct!",
                                Toast.LENGTH_SHORT, R.style.correctAnswerToast).show();

                        // Puzzle will be retrieved, so show loading icon and hide previous image
                        pb_picture_retrieval.setVisibility(View.VISIBLE);
                        iv_picture.setVisibility(View.INVISIBLE);

                        // Get new Puzzle; take into account last passed puzzle
                        retrievePuzzle(mViewModel.lastPassedPuzzleKey);
                        et_answer.setText(""); // Clear EditText

                    } else { // Answer wrong: No answer filled in or wrong answer
                        mViewModel.blurredEntryImage = // apply blur effect
                                applyBlur(mViewModel.retrievedEntry.getPuzzleImage());
                        iv_picture.setImageBitmap(mViewModel.blurredEntryImage);

                        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        et_answer.startAnimation(shake);
                        et_answer.setTextColor(Color.RED);
                    }
                }
            }
        });

        btn_passpuzzle = v.findViewById(R.id.btn_pass);
        btn_passpuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Only enable pass button when we are not loading/retrieving a puzzle
                if (pb_picture_retrieval.getVisibility() == View.INVISIBLE) {
                    StyleableToast.makeText(getContext(), "Skipped",
                            Toast.LENGTH_SHORT, R.style.passedPuzzleToast).show();
                    mViewModel.lastPassedPuzzleKey = mViewModel.currentPuzzleKey;
                    // Removes passed puzzle element from encountered list
                    mViewModel.encounteredPuzzles.remove(mViewModel.lastPassedPuzzleKey);
                    // Retrieve a new puzzle taking into account the last passed key
                    retrievePuzzle(mViewModel.lastPassedPuzzleKey);
                    et_answer.setText(""); // Clear EditText
                }
            }
        });

        if (!mViewModel.gameFinished) { // We are still busy playing the game
            // If we have no puzzle then retrieve one
            if (TextUtils.isEmpty(mViewModel.currentPuzzleKey)) {
                retrievePuzzle("");
            } else { // If we already had a puzzle before onCreateView() was called,
                // we know the screen has been rotated
                iv_picture.setImageBitmap(mViewModel.blurredEntryImage);
                puzzleHint.buildHint(true, tv_hint_stripes, et_answer);
            }
        } else {
            showFinishedScreen();
        }

        return v;
    }

    /**
     * Enables finished layout and disabled and makes invisible all puzzle UI components
     */
    private void showFinishedScreen() {
        mViewModel.gameFinished = true;

        cl_finished.setVisibility(View.VISIBLE);

        // We do it this way since we don't seem able to make the normal layout invisible
        pb_picture_retrieval.setEnabled(false);
        pb_picture_retrieval.setVisibility(View.INVISIBLE);

        btn_submit.setEnabled(false);
        btn_submit.setVisibility(View.INVISIBLE);

        btn_passpuzzle.setEnabled(false);
        btn_passpuzzle.setVisibility(View.INVISIBLE);

        iv_picture.setEnabled(false);
        iv_picture.setVisibility(View.INVISIBLE);

        et_answer.setEnabled(false);
        et_answer.setVisibility(View.INVISIBLE);

        tv_hint_stripes.setEnabled(false);
        tv_hint_stripes.setVisibility(View.INVISIBLE);
    }

    /**
     * Blurs image and sets iv_picture based on current blurMode
     * Afterwards increases blurMode
     * (If max blurMode is reached it returns an unblurred-bitmap
     */
    private Bitmap applyBlur(Uri image_uri) {
        Bitmap blurred_image = null;
        try {
            // Apply blur effect
            blurred_image = BlurBuilder.blur(getContext(), image_uri, mViewModel.blurMode);
            mViewModel.blurMode++; // Increase blur effect for next possible failure
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blurred_image;
    }

    /**
     * Sets retrieveNewPuzzle method call with correct callbacks
     */
    public void retrievePuzzle(String passed_key) {
        // Puzzle will be retrieved, so show loading icon and hide previous image
        pb_picture_retrieval.setVisibility(View.VISIBLE);
        iv_picture.setVisibility(View.INVISIBLE);

        retriever.retrieveNewPuzzle(passed_key, new FileHelper.Callback() {
            @Override
            public void call() {
                showFinishedScreen();
            }
        }, new FileHelper.Callback() {
            @Override
            public void call() {
                // Puzzle is retrieved, so hide loading icon and show image
                pb_picture_retrieval.setVisibility(View.INVISIBLE);
                mViewModel.blurredEntryImage = applyBlur(mViewModel.retrievedEntry
                        .getPuzzleImage()); // apply blur effect
                iv_picture.setImageBitmap(mViewModel.blurredEntryImage);
                iv_picture.setVisibility(View.VISIBLE);

                puzzleHint.buildHint(false, tv_hint_stripes, et_answer);
            }
        }); // Get the first puzzle
    }
}
