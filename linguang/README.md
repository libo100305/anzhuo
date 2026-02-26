# 电话号码识别器

一款基于Android的离线电话号码识别工具应用，支持拍照识别电话号码并与通讯录匹配。

## 核心功能

### 📸 拍照识别
- 调用手机摄像头拍摄包含电话号码的图片
- 使用Google ML Kit进行离线OCR识别
- 支持多种电话号码格式：
  - 手机号码：138-1234-5678, 138 1234 5678, (138)1234-5678
  - 固定电话：010-12345678, 021-12345678
  - 国际号码：+86 138-1234-5678

### 📇 通讯录匹配
- 一键导入手机系统通讯录
- 自动匹配识别出的电话号码对应的联系人
- 本地存储，保护隐私安全

### 📋 识别历史
- 保存所有识别记录
- 显示识别时间、号码和匹配的联系人
- 支持查看和删除历史记录

### 🔒 离线使用
- 所有功能均可离线使用
- 数据本地存储，无需网络连接
- 隐私安全，不上传任何个人信息

## 技术特点

### 架构设计
- **Clean Architecture**：清晰的分层架构
- **MVVM模式**：使用Jetpack Compose和ViewModel
- **依赖注入**：Hilt DI框架
- **响应式编程**：Kotlin Coroutines + Flow

### 核心技术栈
- **Kotlin**：现代化编程语言
- **Jetpack Compose**：声明式UI框架
- **Google ML Kit**：强大的OCR识别能力
- **Room Database**：SQLite数据库封装
- **CameraX**：现代相机API

### 性能优化
- 后台线程处理OCR识别
- 数据库索引优化查询性能
- 图片压缩减少内存占用
- 懒加载提高响应速度

## 安装使用

### 系统要求
- Android 5.0 (API 21) 及以上版本
- 需要相机和通讯录权限

### 安装步骤
1. 下载APK文件
2. 允许安装未知来源应用
3. 安装并打开应用
4. 授予相机和通讯录权限
5. 开始使用！

### 使用方法
1. **首页**：查看功能介绍和快捷入口
2. **扫描**：点击拍照按钮识别电话号码
3. **导入通讯录**：同步手机联系人信息
4. **历史记录**：查看之前的识别结果

## 开发环境

### 项目结构
```
app/
├── src/main/java/com/scanner/phonenumber/
│   ├── data/           # 数据层
│   │   ├── database/   # 数据库相关
│   │   ├── model/      # 数据模型
│   │   └── repository/ # 数据仓库
│   ├── domain/         # 业务逻辑层
│   │   ├── usecase/    # 用例
│   │   └── util/       # 工具类
│   ├── presentation/   # 表示层
│   │   ├── ui/         # UI组件
│   │   ├── viewModel/  # 视图模型
│   │   └── camera/     # 相机管理
│   └── di/            # 依赖注入
└── src/main/res/      # 资源文件
```

### 主要依赖
```gradle
// ML Kit 文本识别
implementation 'com.google.mlkit:text-recognition:16.0.0'

// Room 数据库
implementation "androidx.room:room-runtime:2.6.1"
implementation "androidx.room:room-ktx:2.6.1"

// Hilt 依赖注入
implementation "com.google.dagger:hilt-android:2.48"

// CameraX 相机
implementation "androidx.camera:camera-core:1.3.0"
implementation "androidx.camera:camera-camera2:1.3.0"
```

## 隐私政策

本应用严格遵守隐私保护原则：
- 所有数据仅存储在本地设备
- 不会收集或上传任何个人信息
- 相机权限仅用于拍照识别功能
- 通讯录权限仅用于联系人匹配功能

## 常见问题

### Q: 识别准确率不高怎么办？
A: 请确保拍摄时：
- 光线充足
- 电话号码清晰可见
- 尽量保持水平拍摄
- 避免反光和阴影

### Q: 无法匹配到通讯录联系人？
A: 请检查：
- 是否已导入通讯录
- 电话号码格式是否匹配
- 联系人是否存在于系统通讯录中

### Q: 应用闪退或卡顿？
A: 请尝试：
- 重启应用
- 清除应用缓存
- 检查手机存储空间
- 更新到最新版本

## 版本更新

### v1.0.0 (当前版本)
- ✅ 基础拍照识别功能
- ✅ 通讯录匹配功能
- ✅ 历史记录管理
- ✅ 离线使用支持
- ✅ Material Design 3 界面

## 开源协议

本项目采用 MIT License 开源协议。

## 联系我们

如有任何问题或建议，请通过以下方式联系我们：
- 提交 GitHub Issues
- 发送邮件至：support@example.com

---
*让电话号码识别变得更简单！*