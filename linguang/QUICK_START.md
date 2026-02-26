# 电话号码识别器 - 快速开始指南

## 📱 项目概览

这是一个完整的Android应用项目，实现了拍照识别电话号码的功能。

## 📁 项目结构

```
phone-number-scanner/
├── app/                          # 主应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/scanner/phonenumber/
│   │   │   │   ├── data/         # 数据层
│   │   │   │   ├── domain/       # 业务逻辑层
│   │   │   │   ├── presentation/ # UI层
│   │   │   │   └── di/          # 依赖注入
│   │   │   └── res/             # 资源文件
│   │   └── test/                # 单元测试
│   └── build.gradle            # 模块构建配置
├── gradle/                      # Gradle Wrapper
├── build.gradle                # 顶级构建配置
├── settings.gradle             # 项目设置
├── README.md                   # 详细文档
├── build.bat                   # Windows打包脚本
└── check_env.bat              # 环境检查脚本
```

## 🚀 快速打包步骤

### 方法一：使用现有Android开发环境

1. **检查环境**：
   ```
   双击运行 check_env.bat
   ```

2. **打包应用**：
   ```
   双击运行 build.bat
   ```

3. **获取APK**：
   打包完成后，APK文件位于：
   `app/build/outputs/apk/debug/app-debug.apk`

### 方法二：在Android Studio中打包

1. 用Android Studio打开项目根目录
2. 等待Gradle同步完成
3. 选择 Build → Build Bundle(s) / APK(s) → Build APK(s)
4. 在弹出的通知中点击"locate"查看APK位置

### 方法三：命令行打包

```bash
# 在项目根目录执行
./gradlew assembleDebug

# 或者Windows系统
gradlew.bat assembleDebug
```

## 📋 系统要求

- **Java**: JDK 11 或更高版本
- **Android SDK**: API 21 (Android 5.0) 及以上
- **构建工具**: Android Gradle Plugin 8.2.0
- **磁盘空间**: 至少500MB可用空间

## 🔧 环境变量设置

需要设置以下环境变量：

```
ANDROID_HOME=C:\Users\[用户名]\AppData\Local\Android\Sdk
JAVA_HOME=C:\Program Files\Java\jdk-17
PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
```

## 🎯 应用功能

✅ 拍照识别电话号码（支持多种格式）
✅ 通讯录匹配和导入
✅ 识别历史记录管理
✅ 完全离线使用
✅ Material Design 3 界面

## 📞 技术支持

如遇到打包问题，请检查：
1. Android SDK是否完整安装
2. 环境变量是否正确设置
3. 磁盘空间是否充足
4. 网络连接是否正常（首次构建需要下载依赖）

---
*享受您的电话号码识别器应用！*