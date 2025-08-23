Feature: Notification Utility Test

Background:
  * def testMessage = "Test notification from Karate framework"
  * def testSubject = "Karate Test Notification"

Scenario: Test Email Notification
  * def emailResult = notificationService.sendEmail(testSubject, testMessage, recipients.to, recipients.cc, recipients.bcc)
  * print 'Email notification result:', emailResult

Scenario: Test Slack Notification  
  * def slackResult = notificationService.sendSlack(testMessage, slack.channel)
  * print 'Slack notification result:', slackResult

Scenario: Test Combined Notifications
  * def combinedResult = notificationService.sendBoth(testSubject, testMessage, recipients.to, recipients.cc, recipients.bcc, slack.channel)
  * print 'Combined notification result:', combinedResult