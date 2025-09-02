

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
...
├── src/
│ └── main/
│ ├── java/com/yourapp/face/ # 人脸识别核心代码
│ └── resources/
├── model/
│ └── modelnew.onnx # 人脸识别模型（已修复）
├── pom.xml
└── README.md
...

---

## 🧰 环境准备与安装步骤

### 第一步：安装 JDK

1. 卸载所有旧版 JDK
2. 安装 [OpenJDK 17](https://adoptium.net/zh-CN/download?link=https%3A%2F%2Fgithub.com%2Fadoptium%2Ftemurin17-binaries%2Freleases%2Fdownload%2Fjdk-17.0.16%252B8%2FOpenJDK17U-jdk_x64_windows_hotspot_17.0.16_8.msi&vendor=Adoptium)
3. 设置环境变量 `JAVA_HOME` 为新安装路径

---

### 第二步：安装 C++ 环境（用于 ONNX Runtime）

- 下载并安装 [VC++ Redistributable](https://learn.microsoft.com/zh-cn/cpp/windows/latest-supported-vc-redist?view=msvc-170&utm_source=chatgpt.com)
  - 下载并运行 `VC_redist.x64.exe`

---

### 第三步：配置 ONNX Runtime DLL

1. 下载 ONNX Runtime：
   [onnxruntime-win-x64-1.22.0.zip](https://github.com/microsoft/onnxruntime/releases?utm_source=chatgpt.com)
2. 解压后，将 `onnxruntime.dll` 添加到系统 PATH 或项目资源路径

---

### 第四步：创建 Maven 项目（如未使用该项目模板）

1. IntelliJ IDEA 新建 Maven 项目
2. 设置 `GroupId` 和 `ArtifactId`
3. 修改 `pom.xml`：

```xml
<dependencies>
    <dependency>
        <groupId>com.microsoft.onnxruntime</groupId>
        <artifactId>onnxruntime</artifactId>
        <version>1.22.0</version> <!-- 必须与 DLL 匹配 -->
    </dependency>
</dependencies>


