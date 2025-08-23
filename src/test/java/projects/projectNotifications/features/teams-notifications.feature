@teams
Feature: Teams Notification Tests

Background:
  * def config = read('classpath:projects/projectNotifications/projectNotifications-config.json')
  * def teamsConfig = config.environments.qa.teams
  * def TeamsService = Java.type('utilities.TeamsNotificationService')

Scenario: Send simple Teams notification
  * def result = TeamsService.sendSimpleTeamsMessage(teamsConfig.webhookUrl, 'Test Notification', 'This is a test message from Karate framework')
  * match result.success == true
  * print 'Teams notification result:', result

Scenario: Send formatted Teams test report
  * def result = TeamsService.sendTeamsMessage(teamsConfig.webhookUrl, teamsConfig.title, teamsConfig.subtitle, 'PASSED', 10, 8, 2, '2024-01-15 10:30:00', 'file:///path/to/report.html')
  * match result.success == true
  * print 'Teams test report result:', result

Scenario: Test Teams notification with failed tests
  * def result = TeamsService.sendTeamsMessage(teamsConfig.webhookUrl, teamsConfig.title, teamsConfig.subtitle, 'FAILED', 10, 7, 3, '2024-01-15 10:30:00', null)
  * match result.success == true
  * print 'Teams failed test notification result:', result