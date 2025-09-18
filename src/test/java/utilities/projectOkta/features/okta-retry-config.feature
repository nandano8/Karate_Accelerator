Feature: Configurable Retry Attempts

Background:
  * def RetryService = Java.type('utilities.RetryService')

Scenario: Custom Retry Configuration
  # Configure different retry attempts and delays
  * def maxRetries = 5
  * def delayMs = 1500
  
  # Okta with custom retry
  * def result = RetryService.retryOktaToken(oktaDomain, clientId, clientSecret, scope, maxRetries)
  * print 'Custom retry result:', result

Scenario: Different Retry Scenarios
  # Quick retry (2 attempts, 500ms delay)
  * def quickResult = RetryService.retryOktaToken(oktaDomain, clientId, clientSecret, scope, 2)
  
  # Standard retry (3 attempts, default delay)
  * def standardResult = RetryService.retryOktaToken(oktaDomain, clientId, clientSecret, scope, 3)
  
  # Extended retry (10 attempts)
  * def extendedResult = RetryService.retryOktaToken(oktaDomain, clientId, clientSecret, scope, 10)
  
  * print 'Quick:', quickResult.attempts
  * print 'Standard:', standardResult.attempts  
  * print 'Extended:', extendedResult.attempts