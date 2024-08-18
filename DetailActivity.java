package com.example.suitsappication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.github.clans.fab.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang;
    ImageView detailImage;
    com.github.clans.fab.FloatingActionButton deleteButton, editButton, bookmarkButton;;
    String key = "";
    String imageUrl = "";
    // Gesture detector for swipe
    private GestureDetector gestureDetector;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);
        bookmarkButton = findViewById(R.id.bookmarkButton);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        Button sendSmsButton = findViewById(R.id.sendSmsButton);
        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendItemAsSMS();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

        // ... (previous code)

        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(this, new SwipeGestureListener());
        }
// Set onTouchListener for the ImageView
        detailImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

// ... (remaining code)

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Language", detailLang.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });





    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();

            Log.d("SwipeGesture", "onFling: distanceX = " + distanceX + ", distanceY = " + distanceY);

            if (Math.abs(distanceX) > Math.abs(distanceY) &&
                    Math.abs(distanceX) > SWIPE_THRESHOLD &&
                    Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) { // Check for swipe left (negative distance)
                    Log.d("SwipeGesture", "Swipe from right to left (left to right motion)");
                    onDeleteButtonClick(); // Call delete method on swipe left
                } else {
                    Log.d("SwipeGesture", "Swipe from left to right (right to left motion)");
                }
                return true;
            }
            return false;
        }
    }






    private void sendItemAsSMS() {
        String itemDetails = "Details of the item: " + detailTitle.getText() + ", " + detailDesc.getText();

        // Log the item details
        Log.d("SendSMS", "Item Details: " + itemDetails);

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:+26773065145"));
//        smsIntent.setData(Uri.parse("sms:"));  // You can also specify a phone number here
        smsIntent.putExtra("sms_body", itemDetails);

        try {
            // Log that the SMS intent is being started
            Log.d("SendSMS", "Starting SMS Intent");

            startActivity(smsIntent);
        } catch (ActivityNotFoundException e) {
            // Handle case where no SMS app is available
            Log.e("SendSMS", "SMS app not found", e);
            Toast.makeText(this, "SMS app not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Additional method for handling delete action
    private void onDeleteButtonClick() {
        try {
            Log.d("FirebaseDelete", "onDeleteButtonClick: Deleting item...");

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");

            reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("FirebaseDelete", "Firebase Database delete success");

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FirebaseDelete", "Firebase Storage delete success");
                            Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FirebaseDelete", "Firebase Storage delete failed: " + e.getMessage(), e);
                            Toast.makeText(DetailActivity.this, "Failed to delete from storage", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FirebaseDelete", "Firebase Database delete failed: " + e.getMessage(), e);
                    Toast.makeText(DetailActivity.this, "Failed to delete from database", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("FirebaseDelete", "Error: " + e.getMessage(), e);
            Toast.makeText(DetailActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The gesture controls for item management, including swipe left and right,
     * and shake, are implemented to enhance user interaction. However, it seems
     * that there is an issue with the swipe left functionality as it is not
     * triggering the delete action properly. Further debugging and improvement
     * are needed to ensure smooth and accurate gesture-based item management.
     *
     * Note: Shake gesture is currently not implemented in this version.
     */


}
