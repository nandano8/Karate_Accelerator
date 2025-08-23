Feature: Sample Test
@demo
Scenario: Verify sample API response
    Given url baseUrl
    When method GET
    Then status 200
    And match response.title == 'sunt aut facere repellat provident occaecati excepturi optio reprehenderit'
