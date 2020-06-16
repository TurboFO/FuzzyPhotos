package com.group10.fuzzyphotos.Utils;

/**
 * Should only be used for constants that are shared across the system, not for activity-specific constants
 */
public final class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String Storage_Path = "Image_Uploads/"; // Folder path for Firebase Storage: Storing images of puzzles
    public static final String Database_Path = "Puzzle_Uploads_Database"; // Root Database Name for Firebase Database: Storing puzzles (puzzle_answer, link_to_image_in_storage)
}
