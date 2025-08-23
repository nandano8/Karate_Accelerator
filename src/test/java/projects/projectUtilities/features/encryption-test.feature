Feature: Encryption and Decryption Utilities

Background:
  * def EncryptionService = Java.type('utilities.EncryptionService')

Scenario: AES Encryption and Decryption
  * def originalData = 'This is sensitive data that needs encryption'
  * def key = encryptionKey
  
  # Encrypt data
  * def encryptResult = EncryptionService.encryptAES(originalData, key)
  * match encryptResult.success == true
  * def encryptedData = encryptResult.encrypted
  * print 'Encrypted data:', encryptedData
  
  # Decrypt data
  * def decryptResult = EncryptionService.decryptAES(encryptedData, key)
  * match decryptResult.success == true
  * match decryptResult.decrypted == originalData
  * print 'Decrypted data:', decryptResult.decrypted

Scenario: Generate AES Key
  * def generatedKey = EncryptionService.generateAESKey()
  * match generatedKey == '#string'
  * print 'Generated AES Key:', generatedKey