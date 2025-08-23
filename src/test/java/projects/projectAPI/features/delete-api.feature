Feature: DELETE API Testing

Background:
  * url baseUrl

Scenario: DELETE - Success 200
  Given path '/objects/6'
  When method DELETE
  Then status 200
  And match response.message == 'Object with id = 6 has been deleted.'

Scenario: DELETE - Wrong syntax in URL 400
  Given path '/objects/6?invalid=&'
  When method DELETE
  Then status 400

Scenario: DELETE - Wrong URL 404
  Given path '/automation/6'
  When method DELETE
  Then status 404

Scenario: DELETE - Wrong Method 405
  Given path '/objects'
  When method DELETE
  Then status 405