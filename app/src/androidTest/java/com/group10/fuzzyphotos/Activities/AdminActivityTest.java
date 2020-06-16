package com.group10.fuzzyphotos.Activities;

import android.app.Activity;

import com.group10.fuzzyphotos.R;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class AdminActivityTest {

    @Rule
    public ActivityTestRule<AdminActivity> adminTestRule = new
            ActivityTestRule<>(AdminActivity.class);

    @Test
    public void userCanUpload() {
        //Tests if user is able to press upload button
        // press upload button
        Espresso.onView(withId(R.id.btn_uploadpuzzle)).perform(click());
    }

    @Test
    public void userCanInputAnswer() {
        //Tests if user is able to input answer
        // input answer
        Espresso.onView(withId(R.id.et_solution)).perform(typeText("Test"));
        //close keyboard
        Espresso.closeSoftKeyboard();
    }

    /*@Test
    public void uzerCanMakeImage() {
        //Tests if user is able to press image button
        // press image button
        Espresso.onView(withId(R.id.ibtn_getpicture)).perform(click());
    }*/

    @Test
    public void userCanSubmitAnswer() {
        //Tests if user is able to upload created puzzle
        // input answer
        Espresso.onView(withId(R.id.et_solution)).perform(typeText("Test"));
        //close keyboard
        Espresso.closeSoftKeyboard();
        // press upload button
        Espresso.onView(withId(R.id.btn_uploadpuzzle)).perform(click());
    }
}