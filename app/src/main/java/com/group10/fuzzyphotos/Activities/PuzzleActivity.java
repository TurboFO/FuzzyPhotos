package com.group10.fuzzyphotos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.group10.fuzzyphotos.Fragments.PuzzleFragment;
import com.group10.fuzzyphotos.R;

/**
 * This activity is the base activity for the PuzzleFragment
 */
public class PuzzleActivity extends AppCompatActivity {


    // Firebase
    FirebaseAuth mFirebaseAuth; // Needed for Option-menu to be able to logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // Create admin fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PuzzleFragment.newInstance())
                    .commitNow();
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // Log out option selected
        if (id == R.id.action_settings) {
            mFirebaseAuth.signOut();

            // Create intent to start LogInActivity
            Intent i = new Intent(PuzzleActivity.this, LogInActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
