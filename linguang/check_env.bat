@echo off
echo ========================================
echo Android开发环境检查
echo ========================================

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo ❌ 未找到Java环境
    echo 请安装JDK 11或更高版本
    goto :end
)
echo ✅ Java环境正常

echo.
echo 检查Android SDK...
if defined ANDROID_HOME (
    echo ANDROID_HOME: %ANDROID_HOME%
    if exist "%ANDROID_HOME%" (
        echo ✅ Android SDK路径存在
    ) else (
        echo ❌ Android SDK路径不存在
    )
) else (
    echo ❌ 未设置ANDROID_HOME环境变量
    echo 请设置ANDROID_HOME指向您的Android SDK目录
)

echo.
echo 检查Gradle...
gradle --version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Gradle已安装
    gradle --version | findstr "Gradle"
) else (
    echo ⚠️  Gradle未安装，将使用Gradle Wrapper
)

echo.
echo 检查项目文件...
if exist "build.gradle" (
    echo ✅ 项目构建文件存在
) else (
    echo ❌ 缺少build.gradle文件
)

:end
echo.
echo ========================================
echo 环境检查完成
echo ========================================
pause