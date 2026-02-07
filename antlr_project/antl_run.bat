@echo off
REM -----------------------------
REM One-click ANTLR run for Java
REM -----------------------------

REM Step 1: Generate parser & lexer from Expr.g4
java -jar "D:\antlr\antlr-4.13.2-complete.jar" Expr.g4

REM Step 2: Compile all Java files
javac -cp ".;D:\antlr\antlr-4.13.2-complete.jar" *.java
IF ERRORLEVEL 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Step 3: Run Main.java
java -cp ".;D:\antlr\antlr-4.13.2-complete.jar" Main

pause
