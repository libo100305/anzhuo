#!/bin/bash

# 电话号码识别器构建脚本

echo "开始构建电话号码识别器应用..."

# 清理旧的构建文件
echo "清理构建目录..."
./gradlew clean

# 编译Debug版本
echo "编译Debug版本..."
./gradlew assembleDebug

# 运行单元测试
echo "运行单元测试..."
./gradlew test

# 检查构建结果
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "✅ 构建成功！"
    echo "APK位置: app/build/outputs/apk/debug/app-debug.apk"
    echo "文件大小: $(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)"
else
    echo "❌ 构建失败，请检查错误信息"
    exit 1
fi

echo "构建完成！"