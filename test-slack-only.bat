@echo off
echo Testing Slack Integration...
mvn test -Dkarate.project=projectNotifications -Dtest=projects.projectNotifications.ProjectNotifications_Runner -Dkarate.options="--tags @slack"
pause