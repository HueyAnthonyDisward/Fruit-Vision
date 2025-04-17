package com.example.fruitvision;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ánh xạ các view
        ImageView backButton = findViewById(R.id.back_button);
        TextView fruitTitle = findViewById(R.id.fruit_title);
        TextView fruitDetails = findViewById(R.id.fruit_details);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("fruit_title");
            String details = intent.getStringExtra("fruit_details");

            // Hiển thị dữ liệu
            fruitTitle.setText(title);
            fruitDetails.setText(details);
        }

        // Xử lý sự kiện nhấn nút Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại ActivityCategory
                finish();
            }
        });
    }
}