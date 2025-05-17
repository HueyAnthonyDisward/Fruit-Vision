from PIL import Image
import os

def resize_images_inplace(input_dir, size=(100, 100)):
    # Duyệt qua tất cả các file và thư mục trong input_dir
    for root, _, files in os.walk(input_dir):
        for filename in files:
            # Chỉ xử lý các file ảnh (jpg, jpeg, png)
            if filename.lower().endswith(('.jpg', '.jpeg', '.png')):
                img_path = os.path.join(root, filename)
                try:
                    # Mở và resize ảnh
                    img = Image.open(img_path)
                    img = img.resize(size, Image.Resampling.LANCZOS)
                    # Ghi đè lên file gốc
                    img.save(img_path, quality=95)  # quality=95 để đảm bảo chất lượng
                    print(f"Resized and saved: {img_path}")
                except Exception as e:
                    print(f"Error processing {img_path}: {e}")

# Đường dẫn folder
input_dir = r"D:\2024-2025\Train_Anh"

# Resize ảnh với kích thước 100x100
resize_images_inplace(input_dir, size=(100, 100))