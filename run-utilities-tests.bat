@echo off
echo Running Utilities Tests (Encryption/SFTP)...
mvn test -Dkarate.project=projectUtilities -Dtest=projects.projectUtilities.ProjectUtilities_Runner
pause