package com.group10.fuzzyphotos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.group10.fuzzyphotos.LoginAuthenticator;
import com.group10.fuzzyphotos.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the log in/sign in activity for the app
 */
public class LogInActivity extends AppCompatActivity {

    //initializing variables we are using
    EditText emailEditText;
    EditText passwordEditText;
    Button signInButton;
    TextView logInTextView;
    ProgressBar loginProgress;

    LoginAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // locating ui components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signUpButton);
        logInTextView = findViewById(R.id.registerTextView);
        loginProgress = findViewById(R.id.loginProgress);

        authenticator = new LoginAuthenticator(this); // authenticator for firebase

        //Here is described what happens when you press the sign in button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                //save email which is given by the user in a string
                final String email = emailEditText.getText().toString();
                //save password which is given by the user in a string
                String password = passwordEditText.getText().toString();

                //In case both fields are empty
                if (email.isEmpty() && password.isEmpty()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(LogInActivity.this, "Both Fields are empty",
                            Toast.LENGTH_SHORT).show();
                }
                //In case no email is provided
                else if (email.isEmpty()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    emailEditText.setError("Enter a valid email!");
                    emailEditText.requestFocus();
                }
                //In case no password is provided
                else if (password.isEmpty()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    passwordEditText.setError("Enter a valid password!");
                    passwordEditText.requestFocus();
                }
                //Executed when correct email and password are given
                else {
                    authenticator.normalLogin(email, password, loginProgress);
                }
            }
        });

        /**
         * This gives the textView about going to a different screen it's interactivity
         */
        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Intent to go to register/sign up activity
                Intent intToRegister = new Intent(LogInActivity.this,
                        RegisterActivity.class);

                //Go to next activity
                startActivity(intToRegister);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        authenticator.attemptAutoLogin();
    }
}
