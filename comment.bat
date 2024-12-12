@echo off

REM 检查是否提供了足够的参数
if "%~1"=="" (
    echo 用法: %0 <字符串文本参数>
    exit /b
)

REM 执行Java .class文件
java -cp C:\JohProjects\Other\comments\out\production\comments CommentsMain %CD% %1

REM 等待用户按下任意键以退出脚本
pause
