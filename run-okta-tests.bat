@echo off
echo Running Okta Token Generation Tests...
mvn test -Dkarate.project=projectOkta -Dtest=projects.projectOkta.ProjectOkta_Runner
pause