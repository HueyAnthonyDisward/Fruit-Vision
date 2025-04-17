package com.example.fruitvision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySearchHistory extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        // Xử lý Bottom Navigation
        ImageView cameraButton = findViewById(R.id.bottom_nav).findViewWithTag("Camera");
        if (cameraButton != null) {
            cameraButton.setOnClickListener(v -> {
                Intent intent = new Intent(ActivitySearchHistory.this, ActivityCamera.class);
                startActivity(intent);
                finish();
            });
        }

        ImageView bookButton = findViewById(R.id.bottom_nav).findViewWithTag("Book");
        if (bookButton != null) {
            bookButton.setOnClickListener(v -> {
                Intent intent = new Intent(ActivitySearchHistory.this, ActivityCategory.class);
                startActivity(intent);
                finish();
            });
        }

        ImageView historyButton = findViewById(R.id.bottom_nav).findViewWithTag("History");
        if (historyButton != null) {
            historyButton.setOnClickListener(v -> {
                // Đã ở ActivitySearchHistory, không cần chuyển
            });
        }

        ImageView profileButton = findViewById(R.id.bottom_nav).findViewWithTag("Profile");
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(ActivitySearchHistory.this, ActivityUserboard.class);
                startActivity(intent);
                finish();
            });
        }
    }
}