import os
import torch
import torch.nn as nn
import torch.optim as optim
from torchvision import datasets, transforms
from torch.utils.data import DataLoader
from PIL import Image


# Tùy chỉnh ImageFolder để bỏ qua file lỗi
class CustomImageFolder(datasets.ImageFolder):
    def __getitem__(self, index):
        try:
            return super().__getitem__(index)
        except Exception as e:
            print(f"Skipping corrupted file: {self.samples[index][0]} - Error: {e}")
            return None


def custom_collate_fn(batch):
    batch = [item for item in batch if item is not None]
    if len(batch) == 0:
        return None
    return torch.utils.data.dataloader.default_collate(batch)


# Định nghĩa mô hình
class FruitClassifier(nn.Module):
    def __init__(self, num_classes=10):  # 10 lớp
        super(FruitClassifier, self).__init__()
        self.conv1 = nn.Conv2d(3, 16, kernel_size=3, padding=1)
        self.conv2 = nn.Conv2d(16, 32, kernel_size=3, padding=1)
        self.conv3 = nn.Conv2d(32, 64, kernel_size=3, padding=1)
        self.pool = nn.MaxPool2d(2, 2)
        self.fc1 = nn.Linear(64 * 12 * 12, 512)
        self.fc2 = nn.Linear(512, num_classes)
        self.relu = nn.ReLU()
        self.dropout = nn.Dropout(0.5)

    def forward(self, x):
        x = self.pool(self.relu(self.conv1(x)))
        x = self.pool(self.relu(self.conv2(x)))
        x = self.pool(self.relu(self.conv3(x)))
        x = x.view(-1, 64 * 12 * 12)
        x = self.relu(self.fc1(x))
        x = self.dropout(x)
        x = self.fc2(x)
        return x


# Transform cho dữ liệu
transform = transforms.Compose([
    transforms.Resize((100, 100)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.5, 0.5, 0.5], std=[0.5, 0.5, 0.5])
])

# Load dataset
base_dir = r"D:\2024-2025\Train_Anh"
train_dataset = CustomImageFolder(os.path.join(base_dir, "train"), transform=transform)
val_dataset = CustomImageFolder(os.path.join(base_dir, "validation"), transform=transform)
test_dataset = CustomImageFolder(os.path.join(base_dir, "test"), transform=transform)

train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True, collate_fn=custom_collate_fn)
val_loader = DataLoader(val_dataset, batch_size=32, shuffle=False, collate_fn=custom_collate_fn)
test_loader = DataLoader(test_dataset, batch_size=32, shuffle=False, collate_fn=custom_collate_fn)

# Kiểm tra các lớp
classes = train_dataset.classes
print(f"Classes ({len(classes)}): {classes}")

# Khởi tạo thiết bị và mô hình
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = FruitClassifier(num_classes=len(classes)).to(device)  # num_classes=10

# Huấn luyện mô hình
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)
num_epochs = 20

for epoch in range(num_epochs):
    model.train()
    running_loss = 0.0
    batch_count = 0
    for batch in train_loader:
        if batch is None:
            continue
        images, labels = batch
        images, labels = images.to(device), labels.to(device)
        optimizer.zero_grad()
        outputs = model(images)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()
        running_loss += loss.item()
        batch_count += 1

    if batch_count > 0:
        print(f"Epoch [{epoch + 1}/{num_epochs}], Loss: {running_loss / batch_count:.4f}")
    else:
        print(f"Epoch [{epoch + 1}/{num_epochs}], No valid batches")

    # Đánh giá trên validation
    model.eval()
    correct = 0
    total = 0
    with torch.no_grad():
        for batch in val_loader:
            if batch is None:
                continue
            images, labels = batch
            images, labels = images.to(device), labels.to(device)
            outputs = model(images)
            _, predicted = torch.max(outputs, 1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()

    if total > 0:
        print(f"Validation Accuracy: {100 * correct / total:.2f}%")
    else:
        print("No valid validation data")

# Đánh giá trên test
model.eval()
correct = 0
total = 0
with torch.no_grad():
    for batch in test_loader:
        if batch is None:
            continue
        images, labels = batch
        images, labels = images.to(device), labels.to(device)
        outputs = model(images)
        _, predicted = torch.max(outputs, 1)
        total += labels.size(0)
        correct += (predicted == labels).sum().item()

if total > 0:
    print(f"Test Accuracy: {100 * correct / total:.2f}%")
else:
    print("No valid test data")

# Lưu mô hình
torch.save(model.state_dict(), "fruit_classifier.pth")
model.eval()
example_input = torch.randn(1, 3, 100, 100).to(device)
traced_model = torch.jit.trace(model, example_input)
traced_model.save("fruit_classifier.pt")
traced_model.save("fruit_classifier.ptl")