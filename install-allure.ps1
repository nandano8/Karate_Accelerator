# Download and install Allure command-line tool
$allureVersion = "2.24.0"
$downloadUrl = "https://github.com/allure-framework/allure2/releases/download/$allureVersion/allure-$allureVersion.zip"
$installDir = "$env:USERPROFILE\allure-$allureVersion"
$zipFile = "$env:TEMP\allure-$allureVersion.zip"

Write-Host "Downloading Allure $allureVersion..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $zipFile

Write-Host "Extracting Allure to $installDir..."
Expand-Archive -Path $zipFile -DestinationPath $env:USERPROFILE -Force

Write-Host "Adding Allure to PATH..."
$currentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
$allureBinPath = "$installDir\bin"

if ($currentPath -notlike "*$allureBinPath*") {
    [Environment]::SetEnvironmentVariable("PATH", "$currentPath;$allureBinPath", "User")
    Write-Host "Allure added to PATH. Please restart your command prompt."
}

Write-Host "Cleaning up..."
Remove-Item $zipFile -Force

Write-Host "Allure installation completed!"
Write-Host "Installation directory: $installDir"
Write-Host "Please restart your command prompt and run 'allure --version' to verify installation."