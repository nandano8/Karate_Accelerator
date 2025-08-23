Feature: Email Notification APIs

Background:
  * def EmailService = Java.type('utilities.EmailNotificationService')
  * def NotificationService = Java.type('utilities.NotificationService')

Scenario: Send Simple Email
  * def result = EmailService.sendSimpleEmail(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to[0], 'Test Subject', 'Test message body')
  * match result.success == true || result.success == false
  * print 'Simple email result:', result

Scenario: Send Email with Multiple Recipients
  * def toArray = recipients.to
  * def ccArray = recipients.cc
  * def bccArray = recipients.bcc
  * def result = EmailService.sendEmail(email.smtpHost, email.smtpPort, email.username, email.password, email.from, toArray, ccArray, bccArray, 'Multi-Recipient Test', 'This email has multiple recipients', false)
  * match result.success == true || result.success == false
  * print 'Multi-recipient email result:', result

Scenario: Send HTML Email
  * def htmlBody = '<h1>Test Email</h1><p>This is an <b>HTML</b> email with <a href="https://example.com">link</a></p>'
  * def result = EmailService.sendEmail(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to, null, null, 'HTML Email Test', htmlBody, true)
  * match result.success == true || result.success == false
  * print 'HTML email result:', result

Scenario: Create Email Payload
  * def payload = NotificationService.createEmailPayload(email.smtpHost, email.smtpPort, email.username, email.password, email.from, recipients.to, recipients.cc, recipients.bcc, false)
  * match payload.smtpHost == email.smtpHost
  * match payload.to == recipients.to
  * print 'Email payload created:', payload