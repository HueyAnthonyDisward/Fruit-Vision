package com.example.fruitvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ActivityViewImage extends AppCompatActivity {

    private ImageView imageView, backArrow;
    private Button recognizeButton;
    private TextView resultTextView;
    private Module module;
    // Cập nhật danh sách nhãn
    private String[] classes = {
            "apple", "banana", "carrot", "chillipeper", "grapes",
            "kiwi", "orange", "pineapple", "sweetcorn", "watermelon"
    };
    private Bitmap selectedBitmap;  // Lưu bitmap để dự đoán

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        // Ánh xạ các view
        imageView = findViewById(R.id.selected_image);
        backArrow = findViewById(R.id.back_arrow);
        recognizeButton = findViewById(R.id.recognize_button);
        resultTextView = findViewById(R.id.result_text);

        // Tải mô hình từ assets
        try {
            module = Module.load(assetFilePath("fruit_classifier.ptl"));  // Đảm bảo tên file đúng
        } catch (IOException e) {
            Toast.makeText(this, "Error loading model: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Lấy URI ảnh từ Intent
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
            try {
                // Chuyển URI thành Bitmap để dự đoán
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                Toast.makeText(this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            Toast.makeText(this, "Không thể tải ảnh!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Xử lý sự kiện nhấn nút Back Arrow
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityViewImage.this, ActivityCamera.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nhấn nút Nhận diện
        recognizeButton.setOnClickListener(v -> {
            if (selectedBitmap != null) {
                resultTextView.setText("Predicting...");
                String prediction = classifyImage(selectedBitmap);
                resultTextView.setText("Predicted: " + prediction);
            } else {
                Toast.makeText(ActivityViewImage.this, "No image to classify!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Lấy đường dẫn file từ assets
    private String assetFilePath(String assetName) throws IOException {
        File file = new File(getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    // Dự đoán ảnh
    private String classifyImage(Bitmap bitmap) {
        try {
            // Resize ảnh về 100x100
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

            // Chuyển ảnh thành tensor, chuẩn hóa giống khi huấn luyện
            final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                    bitmap,
                    new float[]{0.5f, 0.5f, 0.5f},  // mean
                    new float[]{0.5f, 0.5f, 0.5f}   // std
            );

            // Dự đoán
            final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
            final float[] scores = outputTensor.getDataAsFloatArray();

            // Tìm lớp có điểm cao nhất
            int maxScoreIdx = -1;
            float maxScore = -Float.MAX_VALUE;
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] > maxScore) {
                    maxScore = scores[i];
                    maxScoreIdx = i;
                }
            }

            return classes[maxScoreIdx];
        } catch (Exception e) {
            Toast.makeText(this, "Error classifying image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "Error";
        }
    }
}