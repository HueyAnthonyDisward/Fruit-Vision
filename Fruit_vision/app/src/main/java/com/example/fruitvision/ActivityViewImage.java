package com.example.fruitvision;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityViewImage extends AppCompatActivity {

    private ImageView imageView, backArrow;
    private Button recognizeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        // Ánh xạ các view
        imageView = findViewById(R.id.selected_image);
        backArrow = findViewById(R.id.back_arrow);
        recognizeButton = findViewById(R.id.recognize_button);

        // Lấy URI ảnh từ Intent
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Không thể tải ảnh!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Xử lý sự kiện nhấn nút Back Arrow
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityViewImage.this, ActivityCamera.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nhấn nút Nhận diện (chưa cần logic nhận diện)
        recognizeButton.setOnClickListener(v -> {
            Toast.makeText(ActivityViewImage.this, "Chức năng nhận diện chưa được triển khai!", Toast.LENGTH_SHORT).show();
        });
    }
}