Feature: Email Reading Tests

Background:
  * def EmailReader = Java.type('utilities.EmailReader')

Scenario: Read latest emails from inbox
  * def emailConfig = 
    """
    {
      host: 'imap.gmail.com',
      username: 'your-email@gmail.com',
      password: 'your-app-password',
      folder: 'INBOX',
      maxEmails: 5
    }
    """
  * def result = EmailReader.readEmails(emailConfig.host, emailConfig.username, emailConfig.password, emailConfig.folder, emailConfig.maxEmails)
  * match result.success == true
  * match result.count >= 0
  * print 'Found', result.count, 'emails'

Scenario: Search for specific email after API call
  # First make your API call that sends email
  * url 'https://your-api-endpoint.com'
  * request { "action": "send-email", "recipient": "test@example.com" }
  * method post
  * status 200
  
  # Wait for email to arrive
  * def sleep = function(ms){ java.lang.Thread.sleep(ms) }
  * call sleep 10000
  
  # Search for the email
  * def emailConfig = 
    """
    {
      host: 'imap.gmail.com',
      username: 'test@example.com',
      password: 'your-app-password',
      folder: 'INBOX',
      subject: 'Your Expected Subject',
      minutes: 5
    }
    """
  * def result = EmailReader.searchEmails(emailConfig.host, emailConfig.username, emailConfig.password, emailConfig.folder, emailConfig.subject, emailConfig.minutes)
  * match result.success == true
  * match result.count > 0
  * print 'Email found:', result.emails[0].subject

Scenario: Verify email content after API trigger
  # Trigger API that sends email
  * url 'https://your-api-endpoint.com'
  * request { "userId": 123, "action": "welcome-email" }
  * method post
  * status 200
  
  # Wait and search for email
  * def sleep = function(ms){ java.lang.Thread.sleep(ms) }
  * call sleep 15000
  
  * def result = EmailReader.searchEmails('imap.gmail.com', 'recipient@gmail.com', 'app-password', 'INBOX', 'Welcome', 10)
  * match result.success == true
  * match result.count > 0
  
  # Verify email content
  * def email = result.emails[0]
  * match email.subject contains 'Welcome'
  * match email.content contains 'Thank you for joining'
  * print 'Email verification passed'