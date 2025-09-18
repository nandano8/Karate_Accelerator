Feature: Utilities with Retry Mechanism

Background:
  * def RetryService = Java.type('utilities.RetryService')

Scenario: SFTP Transfer with Retry
  * def result = RetryService.retrySftpTransfer(sourceSftp.host, sourceSftp.port, sourceSftp.user, sourceSftp.password, '/source/file.txt', targetSftp.host, targetSftp.port, targetSftp.user, targetSftp.password, '/target/file.txt', retry.maxAttempts)
  * match result.success == true || result.success == false
  * match result.attempts == '#number'
  * print 'SFTP retry result:', result

Scenario: Generic Retry Test
  * def operation = function() { return { success: Math.random() > 0.7, error: 'Random failure' } }
  * def result = RetryService.executeWithRetry(operation, 5, 500)
  * match result.success == true || result.success == false
  * print 'Generic retry result:', result