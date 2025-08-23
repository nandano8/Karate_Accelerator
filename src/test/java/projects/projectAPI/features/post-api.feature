Feature: POST API Testing

Background:
  * url baseUrl

Scenario: POST - Success 200
  Given path '/objects'
  And request { "name": "Apple MacBook Pro 16", "data": { "year": 2019, "price": 1849.99, "CPU model": "Intel Core i9", "Hard disk size": "1 TB" } }
  When method POST
  Then status 200
  And match response.id != null
  And match response.name == 'Apple MacBook Pro 16'

Scenario: POST - Wrong syntax in URL 400
  Given path '/objects|'
  And request "name": "Test"
  When method POST
  Then status 400

Scenario: POST - Wrong URL 404
  Given path '/automation'
  And request { "name": "Test" }
  When method POST
  Then status 404

Scenario: POST - Wrong Method 405
  Given path '/objects/1'
  When method PUT
  And request { "name": "Test" }
  Then status 405