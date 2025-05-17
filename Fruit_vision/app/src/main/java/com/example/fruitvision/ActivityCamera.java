package com.example.fruitvision;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityCamera extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_PICK_IMAGE = 101; // Định nghĩa mã yêu cầu cho việc chọn ảnh
    private static final String TAG = "ActivityCamera";
    private PreviewView previewView;
    private ImageView backButton;
    private ImageView captureButton;
    private ImageView navFlash; // Dùng để chuyển đổi camera
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;
    private Camera camera;
    private CameraSelector cameraSelector; // Để theo dõi camera hiện tại
    private boolean isBackCamera = true; // Mặc định là camera sau

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Ánh xạ các view
        previewView = findViewById(R.id.preview_view);
        backButton = findViewById(R.id.back_button);
        captureButton = findViewById(R.id.capture_button);
        navFlash = findViewById(R.id.nav_flash);

        // Khởi tạo executor cho camera
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Khởi tạo cameraSelector mặc định là camera sau
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Kiểm tra quyền camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }

        // Xử lý sự kiện nhấn nút Back
        backButton.setOnClickListener(v -> finish());

        // Xử lý sự kiện nhấn nút chụp ảnh
        captureButton.setOnClickListener(v -> takePhoto());

        // Ánh xạ các nút navigation khác
        ImageView navList = findViewById(R.id.nav_list);
        ImageView navBook = findViewById(R.id.nav_book);
        ImageView navProfile = findViewById(R.id.nav_profile);

        // Xử lý sự kiện cho các nút navigation
        navList.setOnClickListener(v -> {
            // Mở Photos để chọn ảnh
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            try {
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            } catch (Exception e) {
                Toast.makeText(ActivityCamera.this, "Không thể mở thư viện ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Chuyển đổi camera khi nhấn nút flash
        navFlash.setOnClickListener(v -> {
            isBackCamera = !isBackCamera; // Đảo trạng thái camera
            cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(isBackCamera ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT)
                    .build();
            startCamera(); // Bind lại camera với selector mới
            Toast.makeText(ActivityCamera.this,
                    "Đã chuyển sang " + (isBackCamera ? "camera sau" : "camera trước"),
                    Toast.LENGTH_SHORT).show();
        });

        navBook.setOnClickListener(v -> {
            // Chuyển sang ActivityCategory
            Intent intent = new Intent(ActivityCamera.this, ActivityCategory.class);
            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCamera.this, ActivityUserboard.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Chuyển URI sang ActivityViewImage để hiển thị ảnh
                Intent intent = new Intent(ActivityCamera.this, ActivityViewImage.class);
                intent.putExtra("imageUri", selectedImageUri.toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không thể lấy ảnh đã chọn!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                Log.d(TAG, "Camera provider initialized successfully");

                // Cấu hình Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Cấu hình ImageCapture
                imageCapture = new ImageCapture.Builder().build();

                // Giải phóng các binding trước đó
                cameraProvider.unbindAll();

                // Bind camera với lifecycle
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                Log.d(TAG, "Camera bound to lifecycle successfully");

            } catch (Exception e) {
                Log.e(TAG, "Không thể khởi động camera: " + e.getMessage(), e);
                Toast.makeText(this, "Không thể khởi động camera: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera chưa sẵn sàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo tên file ảnh
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String photoFileName = "IMG_" + timeStamp + ".jpg";

        // Cấu hình lưu ảnh vào MediaStore (thư mục công khai)
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, photoFileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/FruitVision");
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        // Cấu hình ImageCapture để lưu vào MediaStore
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build();

        // Chụp ảnh
        imageCapture.takePicture(outputOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> Toast.makeText(ActivityCamera.this,
                        "Ảnh đã được lưu vào thư viện!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> Toast.makeText(ActivityCamera.this,
                        "Lỗi khi chụp ảnh: " + exception.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Quyền camera bị từ chối!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        cameraExecutor.shutdown();
    }
}