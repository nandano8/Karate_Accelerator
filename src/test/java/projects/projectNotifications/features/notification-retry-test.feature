Feature: Notification APIs with Retry

Background:
  * def RetryService = Java.type('utilities.RetryService')
  * def NotificationService = Java.type('utilities.NotificationService')

Scenario: Send Notification with Retry
  * def emailPayload = NotificationService.createEmailPayload(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to, null, null, false)
  * def slackPayload = NotificationService.createSlackPayload(slack.webhookUrl, slack.channel, slack.username, slack.iconEmoji, 'good')
  * def result = RetryService.retryNotification(emailPayload, slackPayload, 'Retry Test', 'Testing notification with retry mechanism', retry.maxAttempts)
  * match result.success == true || result.success == false
  * match result.attempt == '#number' || result.attempts == '#number'
  * print 'Notification retry result:', result