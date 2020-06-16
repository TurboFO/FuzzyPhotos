package com.group10.fuzzyphotos;

import android.graphics.Rect;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.TextView;

import com.group10.fuzzyphotos.Fragments.PuzzleViewModel;

/**
 * class which creates lines for the answer which the user has to guess
 */
public class PuzzleHint {
    PuzzleViewModel viewModel;

    public PuzzleHint(PuzzleViewModel view_model) {
        viewModel = view_model;
    }

    /**
     * Creates the hint stripes and sets editText width to match stripes width
     * screen_rotated true if this method is called due to screen rotation
     */
    public void buildHint(Boolean screen_rotated, TextView stripes, EditText answer) {
        /* Making the lines that answer should be filled in on */
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < viewModel.retrievedEntry.getPuzzleAnswer().length(); i++) {
            s.append("_");
        }
        stripes.setText(s.toString());

        /* Setting the size of the answer field */
        Rect bounds = new Rect();
        stripes.getPaint().getTextBounds(
                stripes.getText().toString(), 0,
                stripes.getText().length(), bounds
        );

        // store width from non-rotated screen
        if (!screen_rotated) { viewModel.boundsWidth = bounds.width();}
        answer.setWidth(viewModel.boundsWidth); // use this stored with in any rotation mode
        answer.setFilters(new InputFilter[]{new InputFilter
                .LengthFilter(viewModel.retrievedEntry.getPuzzleAnswer().length())});
    }
}
