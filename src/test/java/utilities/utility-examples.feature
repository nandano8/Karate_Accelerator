Feature: Utility Examples - How to use the new utility classes

Background:
  * def DatabaseUtil = Java.type('utilities.DatabaseUtility')
  * def NotificationUtil = Java.type('utilities.NotificationUtility')
  * def UtilHelper = Java.type('utilities.UtilityHelper')

Scenario: Database Utility Usage Examples
  # Connect to database
  * def connected = DatabaseUtil.connect('jdbc:postgresql://localhost:5432/testdb', 'user', 'password')
  * match connected == true
  
  # Execute SELECT query
  * def results = DatabaseUtil.executeQuery('SELECT * FROM users WHERE status = ?', [1])
  * match results != null
  
  # Execute INSERT
  * def rowsAffected = DatabaseUtil.executeUpdate('INSERT INTO users (name, email) VALUES (?, ?)', ['John', 'john@test.com'])
  * match rowsAffected == 1
  
  # Check table exists
  * def tableExists = DatabaseUtil.tableExists('users')
  * match tableExists == true
  
  # Get row count
  * def rowCount = DatabaseUtil.getRowCount('users')
  * match rowCount >= 0
  
  # Disconnect
  * def disconnected = DatabaseUtil.disconnect()
  * match disconnected == true

Scenario: Notification Utility Usage Examples
  # Create email configuration
  * def emailConfig = NotificationUtil.createEmailConfig('smtp.gmail.com', 587, 'user@gmail.com', 'password', 'from@test.com', 'to@test.com')
  
  # Send simple email
  * def emailResult = NotificationUtil.sendEmail(emailConfig, 'Test Subject', 'Test Message')
  * match emailResult.success == true
  
  # Send Slack notification
  * def slackResult = NotificationUtil.sendSlack('https://hooks.slack.com/services/...', 'Test message for Slack')
  * match slackResult.success == true
  
  # Send Teams notification
  * def teamsResult = NotificationUtil.sendTeams('https://outlook.office.com/webhook/...', 'Test Title', 'Test message for Teams')
  * match teamsResult.success == true
  
  # Send multi-channel notification
  * def multiResult = NotificationUtil.sendMultiChannel(emailConfig, 'slack-webhook-url', 'teams-webhook-url', 'Multi-channel Test', 'This goes to all channels')
  * match multiResult != null

Scenario: Combined Utility Helper Usage Examples
  # Database operations using UtilHelper
  * def connected = UtilHelper.Database.connect('jdbc:postgresql://localhost:5432/testdb', 'user', 'password')
  * def results = UtilHelper.Database.query('SELECT * FROM users', null)
  * def updated = UtilHelper.Database.update('UPDATE users SET status = ? WHERE id = ?', [1, 123])
  * def disconnected = UtilHelper.Database.disconnect()
  
  # Notification operations using UtilHelper
  * def emailConfig = UtilHelper.Notification.createEmailConfig('smtp.gmail.com', 587, 'user', 'pass', 'from@test.com', 'to@test.com')
  * def emailSent = UtilHelper.Notification.email(emailConfig, 'Subject', 'Message')
  
  # Encryption operations using UtilHelper
  * def encryptResult = UtilHelper.Encryption.encrypt('sensitive-data', 'encryption-key')
  * match encryptResult.success == true
  * def decryptResult = UtilHelper.Encryption.decrypt(encryptResult.encrypted, 'encryption-key')
  * match decryptResult.success == true
  * match decryptResult.decrypted == 'sensitive-data'
  
  # SFTP operations using UtilHelper
  * def uploaded = UtilHelper.SFTP.upload('hostname', 22, 'username', 'password', '/local/file.txt', '/remote/file.txt')
  * def downloaded = UtilHelper.SFTP.download('hostname', 22, 'username', 'password', '/remote/file.txt', '/local/download.txt')
  * def transferred = UtilHelper.SFTP.transfer('host1', 22, 'user1', 'pass1', '/source/file.txt', 'host2', 22, 'user2', 'pass2', '/target/file.txt')
  
  # Okta operations using UtilHelper
  * def tokenResponse = UtilHelper.Okta.getClientCredentialsToken('https://dev-123.okta.com', 'client-id', 'client-secret', 'openid')
  * match tokenResponse.success == true
  * def authCodeResponse = UtilHelper.Okta.getAuthorizationCodeToken('https://dev-123.okta.com', 'client-id', 'client-secret', 'auth-code', 'redirect-uri')
  * match authCodeResponse.success == true
  
  # Allure operations using UtilHelper
  * UtilHelper.Allure.addText('Test Step', 'This is a test step description')
  * UtilHelper.Allure.addJson('Test Data', '{"key": "value"}')
  * UtilHelper.Allure.addTestContext('Feature Name', 'Scenario Name', 'Step Name')
  
  # Utility operations using UtilHelper
  * def timestamp = UtilHelper.Utils.getCurrentTimestamp()
  * def uuid = UtilHelper.Utils.generateUUID()
  * def result = UtilHelper.Utils.createResult(true, 'Operation successful')
  * match result.success == true

Scenario: Real-world Integration Example
  # Start time for performance tracking
  * def startTime = UtilHelper.Utils.getCurrentTimeMillis()
  
  # Connect to database and perform operations
  * def dbConnected = UtilHelper.Database.connect('jdbc:postgresql://localhost:5432/testdb', 'user', 'password')
  * UtilHelper.Allure.addText('Database Connection', 'Connected to test database')
  
  # Execute test query
  * def testResults = UtilHelper.Database.query('SELECT COUNT(*) as total FROM test_table', null)
  * UtilHelper.Allure.addJson('Query Results', testResults)
  
  # Calculate performance
  * def endTime = UtilHelper.Utils.getCurrentTimeMillis()
  * def duration = endTime - startTime
  * UtilHelper.Allure.addPerformance('Database Query', duration, true)
  
  # Send notification about test completion
  * def emailConfig = UtilHelper.Notification.createEmailConfig('smtp.gmail.com', 587, 'test@example.com', 'password', 'test@example.com', 'admin@example.com')
  * def notificationResult = UtilHelper.Notification.email(emailConfig, 'Test Completed', 'Database test completed successfully in ' + duration + 'ms')
  
  # Cleanup
  * def dbDisconnected = UtilHelper.Database.disconnect()
  * UtilHelper.Allure.addText('Test Cleanup', 'Database connection closed')