package com.group10.fuzzyphotos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.group10.fuzzyphotos.Activities.LogInActivity;
import com.muddzdev.styleabletoast.StyleableToast;

/**
 * class which checks whether an account is allowed to be created, if the new account does not have
 * any duplicates in the database and if all requirements are met then this class will also create
 * a new account in the database
 */
public class RegisterAuthenticator {

    private Context context;
    private FirebaseAuth mFirebaseAuth; // the database used for the accounts

    public RegisterAuthenticator(Context activity_context) {
        context = activity_context; // Context of object creator

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Tries to register the user with 'email' and 'password'. If this fails a toast will be shown
     * and the loading progress bar will be made invisible
     */
    public void register(final String email, final String password, final ProgressBar pb) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context,
                        new OnCompleteListener<AuthResult>() {
                            // Response from firebase
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // firebase authentication returned a result so set
                                // loader icon invisible
                                pb.setVisibility(View.INVISIBLE);
                                if (!task.isSuccessful()) {
                                    Exception currentException = task.getException();

                                    if (currentException != null) {
                                        if (currentException instanceof
                                                FirebaseAuthWeakPasswordException) {
                                            StyleableToast.makeText(context,
                                                    "Your password should contain at "
                                                            + "least 6 characters!",
                                                    Toast.LENGTH_LONG, R.style.standardToast)
                                                    .show();
                                        } else if (currentException instanceof
                                                FirebaseAuthUserCollisionException) {
                                            // com-mail enumeration should be prevented,
                                            // but cannot be easily done.
                                            StyleableToast.makeText(context,
                                                    "Your email address is incorrect!",
                                                    Toast.LENGTH_LONG, R.style.standardToast)
                                                    .show();
                                        } else {
                                            StyleableToast.makeText(context,
                                                    "An unknown error occurred, "
                                                            + "please try again later!",
                                                    Toast.LENGTH_LONG, R.style.standardToast)
                                                    .show();
                                        }
                                    } else {
                                        StyleableToast.makeText(context,
                                                "An unknown error occurred, "
                                                        + "please try again later!",
                                                Toast.LENGTH_LONG, R.style.standardToast)
                                                .show();
                                    }
                                } else { //Sign up successful, Now we go to Login screen.
                                    // Register was successful
                                    // Take the user to the login button from which the user can be
                                    // automatically logged in due to credentials stored in cache
                                    context.startActivity(new Intent(context,
                                            LogInActivity.class));
                                }
                            }
                        });
    }
}
