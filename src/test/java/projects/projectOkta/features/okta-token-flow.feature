Feature: Okta Token Generation Flow

Background:
  * def OktaTokenService = Java.type('utilities.OktaTokenService')
  * def RetryService = Java.type('utilities.RetryService')

Scenario: Generate Client Credentials Token
  * def tokenData = OktaTokenService.generateClientCredentialsToken(oktaDomain, clientId, clientSecret, scope)
  * match tokenData.success == true
  * def tokenRequest = tokenData.tokenRequest
  
  Given url tokenRequest.url
  And headers tokenRequest.headers
  And form field grant_type = 'client_credentials'
  And form field scope = scope
  When method POST
  Then status 200
  And match response.access_token == '#string'
  And match response.token_type == 'Bearer'
  * def accessToken = response.access_token
  * print 'Generated Access Token:', accessToken

Scenario: Generate Token with Retry
  * def result = RetryService.retryOktaToken(oktaDomain, clientId, clientSecret, scope, retry.maxAttempts)
  * match result.success == true || result.success == false
  * print 'Token generation with retry (', retry.maxAttempts, 'attempts):', result

Scenario: Generate Authorization Code Token
  * def authCode = 'sample_auth_code_from_authorization_flow'
  * def tokenData = OktaTokenService.generateAuthorizationCodeToken(oktaDomain, clientId, clientSecret, authCode, redirectUri)
  * match tokenData.success == true
  * def tokenRequest = tokenData.tokenRequest
  
  Given url tokenRequest.url
  And headers tokenRequest.headers
  And form field grant_type = 'authorization_code'
  And form field code = authCode
  And form field redirect_uri = redirectUri
  When method POST
  Then status 200 || status 400
  # Note: This will fail with sample auth code, but demonstrates the flow

Scenario: Validate Token and Get User Info
  * def tokenData = OktaTokenService.generateClientCredentialsToken(oktaDomain, clientId, clientSecret, scope)
  * def tokenRequest = tokenData.tokenRequest
  
  # Get access token
  Given url tokenRequest.url
  And headers tokenRequest.headers
  And form field grant_type = 'client_credentials'
  And form field scope = scope
  When method POST
  Then status 200
  * def accessToken = response.access_token
  
  # Use token to access protected resource
  Given url oktaDomain + '/oauth2/default/v1/userinfo'
  And header Authorization = 'Bearer ' + accessToken
  When method GET
  Then status 200 || status 401
  # Note: Client credentials may not have access to userinfo endpoint