Feature: PUT API Testing

Background:
  * url baseUrl

Scenario: PUT - Success 200
  Given path '/objects/7'
  And request { "name": "Apple MacBook Pro 16", "data": { "year": 2019, "price": 2049.99, "CPU model": "Intel Core i9", "Hard disk size": "1 TB", "color": "silver" } }
  When method PUT
  Then status 200
  And match response.id == '7'
  And match response.name == 'Apple MacBook Pro 16'

Scenario: PUT - Wrong syntax in URL 400
  Given path '/objects/7|'
  And request { "name": "Test" }
  When method PUT
  Then status 400

Scenario: PUT - Wrong URL 404
  Given path '/automation/7'
  And request { "name": "Test" }
  When method PUT
  Then status 404

Scenario: PUT - Wrong Method 405
  Given path '/objects'
  And request { "name": "Test" }
  When method PUT
  Then status 405