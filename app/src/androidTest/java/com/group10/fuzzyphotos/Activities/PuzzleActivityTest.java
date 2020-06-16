package com.group10.fuzzyphotos.Activities;

import com.group10.fuzzyphotos.R;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class PuzzleActivityTest {

    @Rule
    public ActivityTestRule<PuzzleActivity> puzzleTestRule = new
            ActivityTestRule<>(PuzzleActivity.class);

    @Test
    public void userCanPassPuzzle() {
        // Tests if user is able pass a puzzle
        // press pass button
        // We look for UI component by ID and then perform a click
        Espresso.onView(withId(R.id.btn_pass)).perform(click());
    }

    @Test
    public void userCanCheckAnswer() {
        // Tests if user is able to press check button
        // press check button
        Espresso.onView(withId(R.id.btn_submit)).perform(click());
    }
}