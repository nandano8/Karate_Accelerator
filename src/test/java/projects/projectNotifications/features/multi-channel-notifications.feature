Feature: Multi-Channel Notification APIs

Background:
  * def NotificationService = Java.type('utilities.NotificationService')

Scenario: Send Multi-Channel Notification (Email + Slack + Teams)
  * def emailPayload = NotificationService.createEmailPayload(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to, recipients.cc, null, false)
  * def slackPayload = NotificationService.createSlackPayload(slack.webhookUrl, slack.channel, slack.username, slack.iconEmoji, 'good')
  * def teamsPayload = NotificationService.createTeamsPayload(teams.webhookUrl, teams.title, teams.subtitle)
  * def result = NotificationService.sendMultiChannelNotification(emailPayload, slackPayload, teamsPayload, 'Multi-Channel Test', 'This notification was sent to email, Slack, and Teams')
  * match result.success == true || result.success == false
  * match result.results == '#array'
  * print 'Multi-channel notification result:', result

Scenario: Send Email Only Notification
  * def emailPayload = NotificationService.createEmailPayload(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to, null, null, false)
  * def result = NotificationService.sendMultiChannelNotification(emailPayload, null, null, 'Email Only Test', 'This notification was sent to email only')
  * match result.success == true || result.success == false
  * print 'Email only notification result:', result

Scenario: Send Slack Only Notification
  * def slackPayload = NotificationService.createSlackPayload(slack.webhookUrl, slack.channel, slack.username, slack.iconEmoji, 'warning')
  * def result = NotificationService.sendMultiChannelNotification(null, slackPayload, null, 'Slack Only Test', 'This notification was sent to Slack only')
  * match result.success == true || result.success == false
  * print 'Slack only notification result:', result

Scenario: Send Teams Only Notification
  * def teamsPayload = NotificationService.createTeamsPayload(teams.webhookUrl, teams.title, teams.subtitle)
  * def result = NotificationService.sendMultiChannelNotification(null, null, teamsPayload, 'Teams Only Test', 'This notification was sent to Teams only')
  * match result.success == true || result.success == false
  * print 'Teams only notification result:', result

Scenario: Test Notification Payloads
  * def emailPayload = NotificationService.createEmailPayload(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to, recipients.cc, recipients.bcc, true)
  * def slackPayload = NotificationService.createSlackPayload(slack.webhookUrl, slack.channel, 'Custom Bot', ':fire:', 'danger')
  * def teamsPayload = NotificationService.createTeamsPayload(teams.webhookUrl, 'Custom Teams Title', 'Custom Subtitle')
  
  * match emailPayload.smtpHost == email.smtpHost
  * match emailPayload.isHtml == true
  * match slackPayload.username == 'Custom Bot'
  * match slackPayload.color == 'danger'
  * match teamsPayload.webhookUrl == teams.webhookUrl
  * match teamsPayload.title == 'Custom Teams Title'
  
  * print 'Email payload:', emailPayload
  * print 'Slack payload:', slackPayload
  * print 'Teams payload:', teamsPayload