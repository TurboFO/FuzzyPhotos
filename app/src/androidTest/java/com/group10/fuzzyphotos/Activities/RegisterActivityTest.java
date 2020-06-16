package com.group10.fuzzyphotos.Activities;

import android.app.Activity;

import com.group10.fuzzyphotos.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.ls.LSOutput;

import java.util.Random;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class RegisterActivityTest {
    /**
     * If you want to use these test, you first have to perform the following!!!
     * 1. open Firebase Data Base
     * 2. look if test@email.com is registered. If the email is registered, DELETE THIS ACCOUNT
     * FROM THE DATA BASE!
     *
     * or you could change the string userName below to an email which is not in the database yet.
     *
     * Tests DO work when email is already in database. But no new email will be put into database!
     */

    @Rule
    public ActivityTestRule<RegisterActivity> signUpTestRule = new
            ActivityTestRule<>(RegisterActivity.class);

    private String userName = "test@email.com";
    private String password = "Test123";

    @Test
    public void userCanEnterEmail() {
        //Tests if user is able to enter an email on the email editView
        //fill in email
        Espresso.onView(withId(R.id.emailEditText)).perform(typeText(userName));
        //close keyboard
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void userCanEnterPassword() {
        //Tests if user is able to enter an password on the password editView
        //fill in password
        Espresso.onView(withId(R.id.passwordEditText)).perform(typeText(password));
        //close keyboard
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void userCanEnterRepeatPassword() {
        //Tests if user is able to enter the repeat password on the repeatPassword editView
        //fill in repeat password
        Espresso.onView(withId(R.id.passwordEditText2)).perform(typeText(password));
        //close keyboard
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void userCanSignUp() {
        //Tests if user is able to sign up with correct credentials
        //fill in email
        Espresso.onView(withId(R.id.emailEditText)).perform(typeText(userName));
        //close keyboard
        Espresso.closeSoftKeyboard();
        //fill in password
        Espresso.onView(withId(R.id.passwordEditText)).perform(typeText(password));
        //close keyboard
        Espresso.closeSoftKeyboard();
        //fill in repeat password
        Espresso.onView(withId(R.id.passwordEditText2)).perform(typeText(password));
        //close keyboard
        Espresso.closeSoftKeyboard();
        //click sign in button
        Espresso.onView(withId(R.id.signUpButton)).perform(click());
    }
}