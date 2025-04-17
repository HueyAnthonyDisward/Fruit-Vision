package com.example.fruitvision;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityRegister extends AppCompatActivity {
    private EditText fullNameEditText, phoneEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private TextInputLayout fullNameLayout, phoneLayout, emailLayout, passwordLayout, confirmPasswordLayout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ các view
        fullNameEditText = findViewById(R.id.full_name);
        phoneEditText = findViewById(R.id.phone);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);

        fullNameLayout = findViewById(R.id.full_name_layout);
        phoneLayout = findViewById(R.id.phone_layout);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);

        Button registerButton = findViewById(R.id.register_button);

        // Xử lý sự kiện nhấn nút Đăng Ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String fullName = fullNameEditText.getText().toString().trim();
                    String phone = phoneEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    // Kiểm tra email đã tồn tại chưa
                    if (dbHelper.checkEmailExists(email)) {
                        emailLayout.setError("Email đã được sử dụng!");
                        return;
                    }

                    // Đăng ký người dùng
                    boolean success = dbHelper.registerUser(fullName, phone, email, password);
                    if (success) {
                        Toast.makeText(ActivityRegister.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển đến ActivityCategory sau khi đăng ký thành công
                        Intent intent = new Intent(ActivityRegister.this, ActivityCategory.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityRegister.this, "Đăng ký thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Kiểm tra Họ và Tên
        if (fullName.isEmpty()) {
            fullNameLayout.setError("Vui lòng nhập họ và tên!");
            isValid = false;
        } else {
            fullNameLayout.setError(null);
        }

        // Kiểm tra SĐT
        if (phone.isEmpty()) {
            phoneLayout.setError("Vui lòng nhập số điện thoại!");
            isValid = false;
        } else if (!phone.matches("\\d{10,11}")) {
            phoneLayout.setError("Số điện thoại không hợp lệ!");
            isValid = false;
        } else {
            phoneLayout.setError(null);
        }

        // Kiểm tra Email
        if (email.isEmpty()) {
            emailLayout.setError("Vui lòng nhập email!");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Email không hợp lệ!");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Kiểm tra Mật Khẩu
        if (password.isEmpty()) {
            passwordLayout.setError("Vui lòng nhập mật khẩu!");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        // Kiểm tra Nhập Lại Mật Khẩu
        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("Vui lòng nhập lại mật khẩu!");
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordLayout.setError("Mật khẩu không khớp!");
            isValid = false;
        } else {
            confirmPasswordLayout.setError(null);
        }

        return isValid;
    }
}