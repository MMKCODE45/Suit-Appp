package com.example.suitsappication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LandingPageActivity extends AppCompatActivity {

    // Set the duration for which the landing page should be displayed in milliseconds
    private static final int SPLASH_DISPLAY_DURATION = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Use a Handler to delay the transition to the login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to navigate to the login activity
                Intent intent = new Intent(LandingPageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}


