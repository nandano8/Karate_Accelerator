@echo off
echo Generating Allure Report...
mvn allure:report
if %ERRORLEVEL% EQU 0 (
    echo Allure report generated successfully!
    echo Opening report in browser...
    start "" "target\allure-report\index.html"
) else (
    echo Failed to generate Allure report
)
pause