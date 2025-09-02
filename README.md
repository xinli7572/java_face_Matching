

https://github.com/user-attachments/assets/4b436cf3-0274-40e9-b599-28e12864108b

# 📌 java_face_Matching

Spring Boot project integrating **ONNX Runtime** to perform AI-powered face recognition. This project uses the lightweight **MobileFaceNet** model from [InsightFace](https://github.com/deepinsight/insightface), converted to ONNX format and executed using Java.

---

## ✅ Project Overview

This project enables:

- ✅ Java-based face recognition using ONNX Runtime  
- ✅ MobileFaceNet ONNX model integration  
- ✅ Feature extraction and face similarity comparison  
- ✅ Spring Boot backend service for inference tasks  

---

## 📁 Project Structure
---
├── src/
│ └── main/
│ ├── java/com/yourapp/face/ # 人脸识别核心代码
│ └── resources/
├── model/
│ └── modelnew.onnx # 人脸识别模型（已修复）
├── pom.xml
└── README.md
---
---

## 🧰 Environment Setup & Installation Steps

### Step 1: Install JDK

1. Uninstall all existing JDK versions
2. Download and install [OpenJDK 17](https://adoptium.net/zh-CN/download?link=https%3A%2F%2Fgithub.com%2Fadoptium%2Ftemurin17-binaries%2Freleases%2Fdownload%2Fjdk-17.0.16%252B8%2FOpenJDK17U-jdk_x64_windows_hotspot_17.0.16_8.msi&vendor=Adoptium)
3. Set the `JAVA_HOME` environment variable to the new JDK installation path

---

### Step 2: Install C++ Runtime (Required by ONNX Runtime)

- Download and install [Microsoft Visual C++ Redistributable](https://learn.microsoft.com/zh-cn/cpp/windows/latest-supported-vc-redist?view=msvc-170&utm_source=chatgpt.com)  
  - Run the `VC_redist.x64.exe` installer

---

### Step 3: Configure ONNX Runtime DLL

1. Download ONNX Runtime:  
   [onnxruntime-win-x64-1.22.0.zip](https://github.com/microsoft/onnxruntime/releases?utm_source=chatgpt.com)
2. Extract the archive and place the `onnxruntime.dll` file in either:
   - a directory included in your system `PATH`, or
   - your project’s resource or native library path

---

### Step 4: Create a Maven Project (if you're not using this project template)

1. Create a new Maven project in IntelliJ IDEA
2. Set the `GroupId` and `ArtifactId`
3. Add the following to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.microsoft.onnxruntime</groupId>
        <artifactId>onnxruntime</artifactId>
        <version>1.22.0</version> <!-- Must match the downloaded DLL version -->
    </dependency>
</dependencies>



