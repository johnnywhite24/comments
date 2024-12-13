@echo off

set "SCRIPT_DIR=%~dp0"

set "LIB_DIR=%SCRIPT_DIR%\out"

java -cp %LIB_DIR% CommentsMain %CD% %1

pause