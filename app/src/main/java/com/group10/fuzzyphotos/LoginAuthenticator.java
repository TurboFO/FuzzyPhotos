package com.group10.fuzzyphotos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group10.fuzzyphotos.Activities.AdminActivity;
import com.group10.fuzzyphotos.Activities.PuzzleActivity;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Arrays;

/**
 * class to authenticate accounts
 */
public class LoginAuthenticator {

    private static final String[] adminEmails = {"admin1@admin.com", "admin2@admin.com",
            "admin3@admin.com"}; // Emails of admin users

    private Context context;
    private FirebaseAuth mFirebaseAuth; // the database used for the accounts
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public LoginAuthenticator(Context activity_context) {
        context = activity_context; // Context of object creator

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Tries to login the user with 'email' and 'password'. If this fails a toast will be shown
     * and the loading progress bar will be made invisible
     */
    public void normalLogin(final String email, final String password, final ProgressBar pb) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                (Activity) context, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) { //something went wrong while signing in
                            pb.setVisibility(View.INVISIBLE);
                            StyleableToast.makeText(context, "Login unsuccessful",
                                    Toast.LENGTH_SHORT, R.style.standardToast).show();
                        } else { // logging in was successful
                            Intent i;

                            /**
                             * We will now set the correct Intent() according which
                             * user is trying to sign in.
                             */
                            if (Arrays.asList(adminEmails).contains(email
                                    .toLowerCase())) {
                                i = new Intent(context,
                                        AdminActivity.class);
                            } else { //the given email is a normal user
                                i = new Intent(context,
                                        PuzzleActivity.class);
                            }
                            //Go to next activity according the new set Intent i
                            context.startActivity(i);
                        }
                    }
                });
    }

    /**
     * Attempt to automatically login if login credentials are stored in cache
     */
    public void attemptAutoLogin() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                //there is an account singed in
                if (mFirebaseUser != null) {
                    StyleableToast.makeText(context, "Logged in",
                            Toast.LENGTH_SHORT, R.style.standardToast).show();

                    //the user will be send to the the puzzleActivity
                    Intent i = new Intent(context, PuzzleActivity.class);
                    context.startActivity(i);
                }
                //there is no account signed in yet, hence show next toast
                else {
                    StyleableToast.makeText(context, "Log in please!",
                            Toast.LENGTH_SHORT, R.style.standardToast).show();
                }
            }
        };
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
