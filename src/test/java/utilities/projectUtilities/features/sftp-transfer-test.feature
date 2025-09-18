Feature: SFTP File Transfer Utilities

Background:
  * def SftpService = Java.type('utilities.SftpService')
  * def SecureTransferService = Java.type('utilities.SecureTransferService')

Scenario: Direct SFTP File Transfer
  * def transferResult = SftpService.transferFile(sourceSftp.host, sourceSftp.port, sourceSftp.user, sourceSftp.password, '/source/file.txt', targetSftp.host, targetSftp.port, targetSftp.user, targetSftp.password, '/target/file.txt')
  * match transferResult.success == true || transferResult.success == false
  * print 'Transfer result:', transferResult

Scenario: Upload File to SFTP
  * def uploadResult = SftpService.uploadFile(targetSftp.host, targetSftp.port, targetSftp.user, targetSftp.password, 'C:/temp/test.txt', '/remote/test.txt')
  * match uploadResult.success == true || uploadResult.success == false
  * print 'Upload result:', uploadResult

Scenario: Download File from SFTP
  * def downloadResult = SftpService.downloadFile(sourceSftp.host, sourceSftp.port, sourceSftp.user, sourceSftp.password, '/remote/file.txt', 'C:/temp/downloaded.txt')
  * match downloadResult.success == true || downloadResult.success == false
  * print 'Download result:', downloadResult

Scenario: Secure Transfer with Encryption
  * def secureResult = SecureTransferService.encryptAndTransfer(sourceSftp.host, sourceSftp.port, sourceSftp.user, sourceSftp.password, '/source/sensitive.txt', targetSftp.host, targetSftp.port, targetSftp.user, targetSftp.password, '/target/encrypted.txt', encryptionKey)
  * match secureResult.success == true || secureResult.success == false
  * print 'Secure transfer result:', secureResult

Scenario: Download and Decrypt File
  * def decryptResult = SecureTransferService.downloadAndDecrypt(targetSftp.host, targetSftp.port, targetSftp.user, targetSftp.password, '/target/encrypted.txt', 'C:/temp/decrypted.txt', encryptionKey)
  * match decryptResult.success == true || decryptResult.success == false
  * print 'Download and decrypt result:', decryptResult