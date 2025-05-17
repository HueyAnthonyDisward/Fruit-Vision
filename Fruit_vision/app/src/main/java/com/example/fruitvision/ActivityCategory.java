package com.example.fruitvision;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ActivityCategory extends AppCompatActivity {
    private List<Fruit> fruitList;
    private List<Fruit> filteredFruitList; // Danh sách trái cây đã lọc
    private FruitAdapter fruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Tạo danh sách trái cây với thông tin chi tiết
        fruitList = new ArrayList<>();
        fruitList.add(new Fruit(
                "QUẢ TÁO",
                "Rosales",
                "Rhamnaceae",
                "Ziziphus",
                R.drawable.apple,
                "Táo ta hay táo chua (danh pháp hai phần: Ziziphus mauritiana) là loài cây ăn quả của vùng nhiệt đới, thuộc về họ Táo (Rhamnaceae). Tại Trung Quốc, có thể gặp loài táo chua, táo Ấn Độ hay táo Điền (táo Vân Nam), táo gai Vân Nam. Cây có thể đạt tới độ rật nhánh thưa, thành chùm ở các khu vực khô và cao tới 12 mét và đạt tuổi thọ 25 năm. Nức cọ ngọn gốc đỏ hoặc Ấn Độ (chùm lưa Ấn Độ) màu trắng, táo đỏ (mắc cọ) tím thẫm ở châu Phi. Quả là loại quả hạch, khi chín mềm, chua, nhiễu nước, có vị ngọt. Các quả chính vẻ ngọt ở các khóm thổi gian khác nhau ngay cả khi chúng còn xanh và thường không chín. Kích thước và hình dạng quả phụ thuộc vào các giống khác nhau trong nhiều loại cây trồng. Quả được sử dụng để ăn khi còn xanh hoặc ngâm muối, sấy khô, làm mứt hoặc làm đồ uống. Nó là một loại quả giàu chất dinh dưỡng và chứa nhiều vitamin C."
        ));
        fruitList.add(new Fruit(
                "QUẢ CHUỐI",
                "Zingiberales",
                "Musaceae",
                "Musa",
                R.drawable.banana,
                "Chuối là loại cây ăn quả phổ biến ở vùng nhiệt đới, thuộc họ Chuối (Musaceae). Cây chuối có thể cao từ 2 đến 9 mét, lá lớn và dài. Quả chuối thường mọc thành nải, có màu vàng khi chín, vị ngọt, mềm. Chuối giàu kali, vitamin B6 và vitamin C, rất tốt cho sức khỏe tim mạch và tiêu hóa."
        ));
        fruitList.add(new Fruit(
                "QUẢ DÂU",
                "Rosaceae",
                "Rosaceae",
                "Fragaria",
                R.drawable.strawberry,
                "Dâu tây (Fragaria) thuộc họ Hoa hồng (Rosaceae), là loại quả mọng, màu đỏ tươi, vị chua ngọt. Dâu tây thường được trồng ở vùng ôn đới và cận nhiệt đới. Quả dâu tây giàu vitamin C, chất xơ và chất chống oxy hóa, rất tốt cho sức khỏe làn da và hệ miễn dịch."
        ));
        fruitList.add(new Fruit(
                "QUẢ ĐU ĐỦ",
                "Brassicales",
                "Caricaceae",
                "Carica",
                R.drawable.papaya,
                "Đu đủ (Carica papaya) thuộc họ Đu đủ (Caricaceae), là loại cây ăn quả nhiệt đới. Quả đu đủ có màu cam khi chín, thịt mềm, ngọt, chứa nhiều hạt đen. Đu đủ giàu vitamin A, C và E, giúp cải thiện tiêu hóa và tăng cường sức khỏe mắt."
        ));

        // Khởi tạo danh sách đã lọc (ban đầu là toàn bộ danh sách)
        filteredFruitList = new ArrayList<>(fruitList);

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.fruit_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fruitAdapter = new FruitAdapter(filteredFruitList);
        recyclerView.setAdapter(fruitAdapter);

        // Thiết lập chức năng tìm kiếm
        EditText searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterFruits(s.toString());
            }
        });

        // Xử lý Bottom Navigation
        ImageView cameraButton = findViewById(R.id.bottom_nav).findViewWithTag("Camera");
        cameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCategory.this, ActivityCamera.class);
            startActivity(intent);
        });

        ImageView bookButton = findViewById(R.id.bottom_nav).findViewWithTag("Book");
        bookButton.setOnClickListener(v -> {
            // Đã ở ActivityCategory, không cần chuyển
        });

        ImageView profileButton = findViewById(R.id.bottom_nav).findViewWithTag("Profile");
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCategory.this, ActivityUserboard.class);
            startActivity(intent);
        });
    }

    private void filterFruits(String query) {
        filteredFruitList.clear();
        if (query.isEmpty()) {
            filteredFruitList.addAll(fruitList);
        } else {
            for (Fruit fruit : fruitList) {
                if (fruit.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredFruitList.add(fruit);
                }
            }
        }
        fruitAdapter.notifyDataSetChanged();
    }
}