@echo off
echo Running Notification Tests (Email/Slack)...
mvn test -Dkarate.project=projectNotifications -Dtest=projects.projectNotifications.ProjectNotifications_Runner
pause