package com.group10.fuzzyphotos.Firebase;

import android.net.Uri;

/**
 * Entry for retrieval from database
 */
public class PuzzleEntry {

    private String puzzleAnswer; // Answer of puzzle
    private Uri puzzleImage; // Reference to image location of puzzle (image is located in storage)

    public PuzzleEntry() {

    }

    public PuzzleEntry(String name, Uri image) {
        this.puzzleAnswer = name;
        this.puzzleImage= image;
    }

    public String getPuzzleAnswer() {
        return puzzleAnswer;
    }
    public Uri getPuzzleImage() {
        return puzzleImage;
    }
}
