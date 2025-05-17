package com.example.fruitvision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;

public class ActivityUserboard extends AppCompatActivity {

    private ImageView imageAvatar;
    private Button btnChangeInfo;
    private Button btnHistory;
    private Button btnLogout;
    private ImageButton navBook;
    private ImageButton navCamera;
    private ImageButton navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_userboard);

        // Ánh xạ các view
        imageAvatar = findViewById(R.id.imageAvatar);
        btnChangeInfo = findViewById(R.id.btnChangeInfo);
        btnHistory = findViewById(R.id.btnHistory);
        btnLogout = findViewById(R.id.btnLogout);
        navBook = findViewById(R.id.navBook);
        navCamera = findViewById(R.id.navCamera);
        navProfile = findViewById(R.id.navProfile);

        // Xử lý sự kiện cho nút Thay đổi thông tin
        btnChangeInfo.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityUserboard.this, ActivityChangeInfo.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện cho nút Lịch sử tra cứu
        btnHistory.setOnClickListener(v -> {
            Toast.makeText(ActivityUserboard.this, "Chức năng lịch sử tra cứu chưa được triển khai!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện cho nút Đăng xuất
        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(ActivityUserboard.this,MainActivity.class);
            startActivity(intent);
            finish(); // Đóng ActivityUserboard
        });

        btnHistory.setOnClickListener(v->{
            Intent intent = new Intent(ActivityUserboard.this, ActivitySearchHistory.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện cho bottom navigation
        navBook.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityUserboard.this, ActivityCategory.class);
            startActivity(intent);
            finish();
        });

        navCamera.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityUserboard.this, ActivityCamera.class);
            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            // Đã ở ActivityUserboard, không cần chuyển
            Toast.makeText(ActivityUserboard.this, "Bạn đang ở trang Profile!", Toast.LENGTH_SHORT).show();
        });
    }
}