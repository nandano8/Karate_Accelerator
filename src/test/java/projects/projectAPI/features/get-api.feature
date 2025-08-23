Feature: GET API Testing

Background:
  * url baseUrl

Scenario: GET - Success 200
  Given path '/objects'
  When method GET
  Then status 200
  And match response != null

Scenario: GET Single Object - Success 200
  Given path '/objects/7'
  When method GET
  Then status 200
  And match response.id == '7'

Scenario Outline: GET Single Object - Success 200
  Given path <endpoint>
  When method GET
  Then status 200
  And match response.id == <id>

  Examples:
  |   endpoint   | id | 
  |'/objects/1'  |  1 |
  |'/objects/1'  |  5 |
  |'/objects/1'  | 10 |

Scenario: GET All Objects - Success 200
  Given path '/objects'
  When method GET
  Then status 200
  And match response.id == '1'

Scenario: GET - Wrong syntax in URL 400
  Given path '/objects|'
  When method GET
  Then status 400

Scenario: GET - Wrong URL 404
  Given path '/automation'
  When method GET
  Then status 404

Scenario: GET - Wrong Method 405
  Given path '/objects'
  When method DELETE
  Then status 405