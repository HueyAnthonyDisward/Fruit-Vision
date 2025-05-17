import os
import torch
from flask import Flask, request, render_template
from PIL import Image
from torchvision import transforms
import torch.jit

# Khởi tạo Flask app
app = Flask(__name__)

# Đường dẫn thư mục uploads
UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# Danh sách lớp (10 loại trái cây)
CLASSES = [
    "apple", "banana", "carrot", "chilli pepper", "grapes",
    "kiwi", "orange", "pineapple", "sweetcorn", "watermelon"
]

# Load mô hình PyTorch
model_path = os.path.join("uploads", "fruit_classifier.ptl")
model = torch.jit.load(model_path)
model.eval()

# Transform cho ảnh đầu vào
transform = transforms.Compose([
    transforms.Resize((100, 100)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.5, 0.5, 0.5], std=[0.5, 0.5, 0.5])
])


# Hàm dự đoán
def predict_image(image_path):
    # Mở và xử lý ảnh
    image = Image.open(image_path).convert("RGB")
    image = transform(image)
    image = image.unsqueeze(0)  # Thêm batch dimension

    # Dự đoán
    with torch.no_grad():
        outputs = model(image)
        _, predicted = torch.max(outputs, 1)
        predicted_class = CLASSES[predicted.item()]

    return predicted_class


# Route cho trang chủ
@app.route('/', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        # Kiểm tra file tải lên
        if 'file' not in request.files:
            return render_template('index.html', error="No file uploaded")
        file = request.files['file']
        if file.filename == '':
            return render_template('index.html', error="No file selected")

        # Lưu file
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
        file.save(file_path)

        # Dự đoán
        try:
            predicted_class = predict_image(file_path)
            return render_template('index.html', prediction=predicted_class, image_path=file_path)
        except Exception as e:
            return render_template('index.html', error=f"Error processing image: {str(e)}")

    return render_template('index.html')


if __name__ == '__main__':
    app.run(debug=True)