Feature: Simple Okta Token Test

Background:
  * def OktaTokenService = Java.type('utilities.OktaTokenService')

Scenario: Test Token Service Utility
  * def result = OktaTokenService.generateClientCredentialsToken(oktaDomain, clientId, clientSecret, scope)
  * match result.success == true
  * match result.tokenRequest.url == oktaDomain + '/oauth2/default/v1/token'
  * match result.tokenRequest.method == 'POST'
  * match result.encodedCredentials == '#string'
  * print 'Token request prepared successfully'
  * print 'URL:', result.tokenRequest.url
  * print 'Encoded credentials:', result.encodedCredentials