package com.group10.fuzzyphotos.Activities;

import com.group10.fuzzyphotos.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LogInActivityTest {
    /**
     * If you want to use these test, you first have to perform the following!!!
     * 1. open the app
     * 2. navigate to the signIn activity
     * 3. make sure you will stay in this screen, if you are navigated into another screen because
     * you are already signed in PLEASE SIGN OUT!
     */

    @Rule
    public ActivityTestRule<LogInActivity> signInTestRule = new
            ActivityTestRule<>(LogInActivity.class);

    private String userName = "user@user.com";
    private String password = "user123";

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
    public void userCanSignIn() {
        //Tests if user is able to sign in with correct credentials
        //fill in email
        Espresso.onView(withId(R.id.emailEditText)).perform(typeText(userName));
        //close keyboard
        Espresso.closeSoftKeyboard();
        //fill in password
        Espresso.onView(withId(R.id.passwordEditText)).perform(typeText(password));
        //close keyboard
        Espresso.closeSoftKeyboard();
        //click sign in button
        Espresso.onView(withId(R.id.signUpButton)).perform(click());
    }
}