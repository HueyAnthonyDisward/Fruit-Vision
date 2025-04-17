package com.example.fruitvision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private TextInputLayout emailLayout, passwordLayout;
    private DatabaseHelper dbHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ các view
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        // Xử lý show/hide mật khẩu
        passwordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_eye, 0);
                        isPasswordVisible = false;
                    } else {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_eye, 0);
                        isPasswordVisible = true;
                    }
                    return true;
                }
            }
            return false;
        });

        // Xử lý nút Đăng Nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    // Kiểm tra đăng nhập
                    if (dbHelper.loginUser(email, password)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("currentEmail", email);
                        editor.apply();
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển đến Activity camera
                        Intent intent = new Intent(MainActivity.this, ActivityCamera.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Xử lý nút Đăng Ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityRegister.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

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

        return isValid;
    }
}