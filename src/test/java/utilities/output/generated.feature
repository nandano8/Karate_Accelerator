Feature: Generated from cURL

Background:
  Given url '"https://api.example.com/users/12345?notify=true&adminOverride=false"'
  And header Authorization = 'Bearer your_token_ABC123456'
  And header Content-Type = 'application/json'
  And header X-Request-ID = '7890-update-user'

Scenario: PUT request to "https://api.example.com/users/12345?notify=true&adminOverride=false"
  And request
    """
{
      
    "name":  "Meghna Chakraborty",
      
    "email":  "meghna@example.com",
      
    "phone":  "+91-9876543210",
      
    "address":  {
            
      "line1":  "123 Park Street",
            
      "line2":  "Apt 45B",
            
      "city":  "Kolkata",
            
      "state":  "West Bengal",
            
      "postalCode":  "700016",
            
      "country":  "India"
    
      },
      
    "preferences":  {
            
      "newsletter":  true,
            
      "notifications":  {
                  
        "email":  true,
                  
        "sms":  false,
                  
        "push":  true
      
            }
    
      },
      
    "roles":  [
            "user",
             "beta-tester"
      ]
  
}
    """
  When method put
  Then status 200
  And match response == {}  # TODO: Replace with expected response body
