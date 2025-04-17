package com.example.fruitvision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityChangeInfo extends AppCompatActivity {

    private EditText editFullName, editPhone, editEmail, editOldPassword, editNewPassword, editConfirmPassword;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        // Ánh xạ các view
        editFullName = findViewById(R.id.editFullName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editOldPassword = findViewById(R.id.editOldPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSave = findViewById(R.id.btnSave);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Lấy email của tài khoản hiện tại từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentEmail = sharedPreferences.getString("currentEmail", "");
        if (currentEmail.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản! Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin hiện tại của người dùng
        loadUserInfo();

        // Xử lý sự kiện nhấn nút Lưu thay đổi
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void loadUserInfo() {
        // Lấy thông tin từ cơ sở dữ liệu (không lấy password)
        Cursor cursor = dbHelper.getUser(currentEmail);
        if (cursor != null && cursor.moveToFirst()) {
            int fullNameIndex = cursor.getColumnIndex("full_name");
            int phoneIndex = cursor.getColumnIndex("phone");
            int emailIndex = cursor.getColumnIndex("email");

            // Kiểm tra xem các cột có tồn tại không
            if (fullNameIndex == -1 || phoneIndex == -1 || emailIndex == -1) {
                Toast.makeText(this, "Lỗi: Không tìm thấy cột dữ liệu trong cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            String fullName = cursor.getString(fullNameIndex);
            String phone = cursor.getString(phoneIndex);
            String email = cursor.getString(emailIndex);

            // Hiển thị thông tin lên giao diện
            editFullName.setText(fullName != null ? fullName : "");
            editPhone.setText(phone != null ? phone : "");
            editEmail.setText(email != null ? email : "");
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng với email: " + currentEmail, Toast.LENGTH_LONG).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void saveChanges() {
        String fullName = editFullName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String oldPassword = editOldPassword.getText().toString().trim();
        String newPassword = editNewPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        // Kiểm tra các trường bắt buộc (trừ email vì không cho đổi)
        if (fullName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mật khẩu sẽ được giữ nguyên nếu không nhập mật khẩu mới
        String passwordToSave = dbHelper.getPassword(currentEmail);
        if (passwordToSave == null) {
            Toast.makeText(this, "Không tìm thấy thông tin mật khẩu. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra nếu người dùng muốn đổi mật khẩu
        if (!newPassword.isEmpty() || !confirmPassword.isEmpty() || !oldPassword.isEmpty()) {
            // Nếu bất kỳ trường mật khẩu nào được điền, yêu cầu tất cả các trường
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu cũ
            if (!oldPassword.equals(passwordToSave)) {
                Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu mới và xác nhận mật khẩu mới có khớp không
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra mật khẩu mới có hợp lệ không (tối thiểu 6 ký tự)
            if (newPassword.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mật khẩu mới có thể giống mật khẩu cũ, không cần kiểm tra
            passwordToSave = newPassword;
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        }

        // Cập nhật thông tin vào cơ sở dữ liệu
        boolean updated = dbHelper.updateUser(currentEmail, fullName, phone, passwordToSave);
        if (updated) {
            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();

            // Quay lại ActivityUserboard
            Intent intent = new Intent(ActivityChangeInfo.this, ActivityUserboard.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}