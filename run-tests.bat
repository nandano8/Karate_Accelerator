@echo off
echo Running Karate tests...
mvn test -Dtest=ProjectSQL_Runner -Dproject=projectSQL

echo Sending Teams notification...
powershell -ExecutionPolicy Bypass -File send-teams-notification.ps1

echo Done!