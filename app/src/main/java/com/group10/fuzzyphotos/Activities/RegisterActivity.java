package com.group10.fuzzyphotos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.group10.fuzzyphotos.R;
import com.group10.fuzzyphotos.RegisterAuthenticator;
import com.muddzdev.styleabletoast.StyleableToast;

/**
 * This is the register/sign up activity for the app
 */
public class RegisterActivity extends AppCompatActivity {

    // UI - Components
    EditText et_email;
    EditText et_password;
    EditText et_password_repeat;
    Button btn_signup;
    TextView tv_register;

    ProgressBar registerProgress;
    RegisterAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI components
        et_email = findViewById(R.id.emailEditText);
        et_password = findViewById(R.id.passwordEditText);
        et_password_repeat = findViewById(R.id.passwordEditText2);
        btn_signup = findViewById(R.id.signUpButton);
        tv_register = findViewById(R.id.registerTextView);
        registerProgress = findViewById(R.id.registerProgress);

        // authenticator for firebase
        authenticator = new RegisterAuthenticator(this);

        // Sign up button pressed
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set loader icon visible
                registerProgress.setVisibility(View.VISIBLE);

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String passwordControl = et_password_repeat.getText().toString();

                //In case both fields are empty
                if (email.isEmpty() && password.isEmpty()) {
                    // we have a response so set loader icon invisible
                    registerProgress.setVisibility(View.INVISIBLE);
                    StyleableToast.makeText(RegisterActivity.this, "All Fields are empty",
                            Toast.LENGTH_SHORT, R.style.standardToast).show();
                } else if (email.isEmpty()) { //In case no email is provided
                    // we have a response so set loader icon invisible
                    registerProgress.setVisibility(View.INVISIBLE);
                    et_email.setError("Enter a valid email!");
                    et_email.requestFocus();
                } else if (password.isEmpty()) { //In case no password is provided
                    // we have a response so set loader icon invisible
                    registerProgress.setVisibility(View.INVISIBLE);
                    et_password.setError("Enter a valid password!");
                    et_password.requestFocus();
                } else if (passwordControl.isEmpty()) { //In case no password Control is provided
                    // we have a response so set loader icon invisible
                    registerProgress.setVisibility(View.INVISIBLE);
                    et_password_repeat.setError("Re-enter your password!");
                    et_password_repeat.requestFocus();
                } else if (!(password.equals(passwordControl))) { //Passwords are not the same
                    // we have a response so set loader icon invisible
                    registerProgress.setVisibility(View.INVISIBLE);
                    StyleableToast.makeText(RegisterActivity.this,
                            "Passwords are not equal!", Toast.LENGTH_SHORT,
                            R.style.standardToast).show();
                } else { //Executed when correct email is given and password is given twice
                    // correctly
                    // Attempts to authenticate given credentials with firebase authentication
                    // We need to pass registerProgress as a parameter since the firebase
                    // authentication
                    // will run in a separate thread and therefore we don't have to wait here
                    authenticator.register(email, password, registerProgress);
                }
            }
        });

        // Already have an account textView pressed
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take the user to the login activity
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
