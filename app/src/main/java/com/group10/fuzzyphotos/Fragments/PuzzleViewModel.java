package com.group10.fuzzyphotos.Fragments;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.group10.fuzzyphotos.Firebase.PuzzleEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * class with initializations
 */
public class PuzzleViewModel extends ViewModel {

    public PuzzleEntry retrievedEntry = null; // Currently shown puzzle entry (null if none)
    Bitmap blurredEntryImage = null; // Currently shown blurred image
    // Stores database key of all puzzles encountered
    public List<String> encounteredPuzzles = new ArrayList<String>();
    public String currentPuzzleKey; // Key of current puzzle shown
    String lastPassedPuzzleKey = ""; // Key of last passed puzzle
    public int blurMode = 1; // 1 = max blur ; 5 = no blur
    boolean gameFinished = false;

    public int boundsWidth; // For handling screen orientation change
}
