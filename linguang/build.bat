@echo off
echo ========================================
echo 电话号码识别器 - APK打包脚本
echo ========================================

echo 正在清理旧的构建文件...
call gradlew clean

echo 正在编译Debug版本...
call gradlew assembleDebug

echo 正在运行单元测试...
call gradlew test

echo.
echo ========================================
echo 构建完成！
echo ========================================

if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ✅ APK生成成功！
    echo 文件位置: app\build\outputs\apk\debug\app-debug.apk
    for %%A in ("app\build\outputs\apk\debug\app-debug.apk") do echo 文件大小: %%~zA 字节
    echo.
    echo 📱 安装提示：
    echo 1. 将APK文件传输到Android设备
    echo 2. 在设备上允许安装未知来源应用
    echo 3. 点击APK文件进行安装
) else (
    echo ❌ APK生成失败，请检查错误信息
    echo 请确保：
    echo 1. 已安装Android SDK
    echo 2. ANDROID_HOME环境变量已设置
    echo 3. 已安装必要的构建工具
)

pause