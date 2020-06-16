package com.group10.fuzzyphotos.Firebase;

/**
 * Entry for submission to database
 */
public class PuzzleEntryInfo {

    private String puzzleAnswer; // Answer of puzzle
    private String puzzleImageRef; // Reference to image location of puzzle (image is located in storage)

    public PuzzleEntryInfo() {

    }

    public PuzzleEntryInfo(String name, String ref) {
        this.puzzleAnswer = name;
        this.puzzleImageRef= ref;
    }

    public String getPuzzleAnswer() {
        return puzzleAnswer;
    }

    public String getPuzzleImageRef() {
        return puzzleImageRef;
    }
}
