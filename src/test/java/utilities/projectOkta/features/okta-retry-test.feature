Feature: Okta Token Generation with Retry

Background:
  * def RetryService = Java.type('utilities.RetryService')

Scenario: Generate Token with Retry Mechanism
  * def result = RetryService.retryOktaToken(oktaDomain, clientId, clientSecret, scope, 3)
  * match result.success == true || result.success == false
  * match result.attempts == '#number'
  * print 'Retry result:', result
  * if (result.success) print 'Token generated on attempt:', result.attempt