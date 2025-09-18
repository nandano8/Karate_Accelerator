# ExaAPI - Comprehensive Test Automation Framework

## 🎯 Overview
ExaAPI is a comprehensive Karate-based test automation framework with integrated database testing, notifications, security utilities, and reporting capabilities.

## 📋 Table of Contents
- [Database Testing (SQL)](#database-testing-sql)
- [Notification System](#notification-system)
- [Security & Encryption](#security--encryption)
- [SFTP Operations](#sftp-operations)
- [Okta Authentication](#okta-authentication)
- [Allure Reporting](#allure-reporting)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)

---

## 🗄️ Database Testing (SQL)

### Features
- **PostgreSQL Integration**: Full JDBC support for PostgreSQL databases
- **Connection Management**: Automatic connection pooling and cleanup
- **Query Operations**: SELECT, INSERT, UPDATE, DELETE operations
- **Parameter Binding**: Safe parameterized queries to prevent SQL injection
- **Allure Integration**: Automatic query logging and result attachments

### Usage
```gherkin
Background:
  * def dbUrl = 'jdbc:postgresql://localhost:5432/database_name'
  * def dbUser = 'username'
  * def dbPassword = 'password'
  * db.connect(dbUrl, dbUser, dbPassword)

Scenario: Database Operations
  # SELECT Query
  * def results = db.select('SELECT * FROM users WHERE status = ?', [1])
  * match results.size() > 0
  
  # INSERT Operation
  * def rowsAffected = db.insert('INSERT INTO users (name, email) VALUES (?, ?)', ['John', 'john@example.com'])
  * match rowsAffected == 1
  
  # UPDATE Operation
  * def updated = db.update('UPDATE users SET status = ? WHERE id = ?', [0, 123])
  
  # DELETE Operation
  * def deleted = db.delete('DELETE FROM users WHERE status = ?', [0])
  
  * db.disconnect()
```

### Key Files
- `utilities/KarateDatabaseService.java` - Core database operations
- `utilities/projectSQL/features/database-test.feature` - Example usage

---

## 📧 Notification System

### Supported Channels
- **Email**: SMTP-based email notifications with attachments
- **Slack**: Webhook-based Slack channel notifications
- **Microsoft Teams**: Teams channel notifications with action buttons

### Features
- **Multi-Channel**: Send to all channels simultaneously
- **Rich Content**: HTML emails, formatted Slack messages, Teams cards
- **Report Attachments**: Automatic zipped report attachments via email
- **Retry Logic**: Configurable retry mechanism for failed notifications
- **Template Support**: Customizable message templates

### 🔗 Teams Webhook Setup Guide

#### Step 1: Create Teams Workflow
1. **Access Power Automate**:
   - Go to [Power Automate](https://make.powerautomate.com/)
   - Sign in with your Microsoft 365 account

2. **Create New Flow**:
   - Click "+ Create" → "Instant cloud flow"
   - Name: "Test Results Notification"
   - Trigger: "When a HTTP request is received"
   - Click "Create"

#### Step 2: Configure HTTP Trigger
1. **Set Request Schema**:
   ```json
   {
     "type": "object",
     "properties": {
       "@type": { "type": "string" },
       "@context": { "type": "string" },
       "summary": { "type": "string" },
       "sections": {
         "type": "array",
         "items": {
           "type": "object",
           "properties": {
             "activityTitle": { "type": "string" },
             "activitySubtitle": { "type": "string" },
             "facts": {
               "type": "array",
               "items": {
                 "type": "object",
                 "properties": {
                   "name": { "type": "string" },
                   "value": { "type": "string" }
                 }
               }
             }
           }
         }
       }
     }
   }
   ```

#### Step 3: Add Compose Action
1. **Add New Step**: Click "+ New step"
2. **Search**: Type "Compose" and select "Compose" action
3. **Configure Adaptive Card**:
   ```json
   {
     "type": "AdaptiveCard",
     "$schema": "http://adaptivecards.io/schemas/adaptive-card.json",
     "version": "1.4",
     "body": [
       {
         "type": "TextBlock",
         "text": "@{triggerBody()?['sections']?[0]?['activityTitle']}",
         "weight": "Bolder",
         "size": "Large",
         "color": "@{if(contains(triggerBody()?['sections']?[0]?['facts']?[0]?['value'], 'PASSED'), 'Good', 'Attention')}"
       },
       {
         "type": "TextBlock",
         "text": "@{triggerBody()?['sections']?[0]?['activitySubtitle']}",
         "size": "Medium",
         "isSubtle": true
       },
       {
         "type": "FactSet",
         "facts": "@triggerBody()?['sections']?[0]?['facts']"
       }
     ],
     "actions": [
       {
         "type": "Action.OpenUrl",
         "title": "📋 View Karate Report",
         "url": "@{triggerBody()?['potentialAction']?[0]?['targets']?[0]?['uri']}"
       },
       {
         "type": "Action.OpenUrl",
         "title": "📊 View Allure Report",
         "url": "@{triggerBody()?['potentialAction']?[1]?['targets']?[0]?['uri']}"
       }
     ]
   }
   ```

#### Step 4: Add Teams Post Action
1. **Add New Step**: Click "+ New step"
2. **Search**: Type "Teams" and select "Post card in a chat or channel"
3. **Configure**:
   - **Post as**: Flow bot
   - **Post in**: Channel
   - **Team**: Select your team
   - **Channel**: Select target channel (e.g., "Test Results")
   - **Adaptive Card**: Select "Compose" output from previous step

#### Step 5: Save and Get Webhook URL
1. **Save Flow**: Click "Save"
2. **Copy HTTP URL**: From the HTTP trigger step
3. **Use in Configuration**: Add URL to your config file

### Configuration
```json
{
  "email": {
    "enabled": true,
    "smtpHost": "smtp.gmail.com",
    "smtpPort": 587,
    "username": "your-email@gmail.com",
    "password": "app-password"
  },
  "slack": {
    "enabled": true,
    "webhookUrl": "https://hooks.slack.com/services/...",
    "channel": "#test-results"
  },
  "teams": {
    "enabled": true,
    "webhookUrl": "https://prod-xx.eastus.logic.azure.com:443/workflows/.../triggers/manual/paths/invoke"
  }
}
```

### 🎨 Adaptive Card Designer
Use [Adaptive Cards Designer](https://adaptivecards.io/designer/) to:
- **Design Cards**: Create custom card layouts
- **Preview**: See how cards look in Teams
- **Export JSON**: Get card schema for Power Automate
- **Test Data**: Use sample JSON to test designs

### 📱 Teams Notification Features
- **Rich Cards**: Formatted with colors, icons, and sections
- **Action Buttons**: Direct links to Karate and Allure reports
- **Conditional Formatting**: Green for passed, red for failed tests
- **Fact Sets**: Organized display of test metrics
- **Responsive Design**: Works on desktop and mobile Teams apps
```

### Usage
```java
// Automatic notifications after test execution
TestLifecycleHook.sendTestCompletionNotification(results, "projectName");

// Manual notifications
NotificationService.sendMultiChannelNotification(emailConfig, slackConfig, teamsConfig, subject, message);
```

### 🔧 Webhook Workflow Summary
```
📥 Teams Webhook Request Received
     ↓
🔄 Compose (JSON → Adaptive Card)
     ↓
📤 Post Card in Teams Channel
```

**Flow Steps**:
1. **HTTP Trigger**: Receives JSON payload from framework
2. **Compose**: Transforms JSON to Adaptive Card format
3. **Teams Action**: Posts formatted card to specified channel

### Key Files
- `utilities/EmailNotificationService.java` - Email functionality
- `utilities/SlackNotificationService.java` - Slack integration
- `utilities/TeamsNotificationService.java` - Teams integration
- `utilities/NotificationService.java` - Multi-channel orchestration
- `utilities/projectNotifications/projectNotifications-config.json` - Notification configuration

---

## 🔐 Security & Encryption

### Features
- **AES Encryption**: Industry-standard AES-256 encryption
- **Key Management**: Secure key generation and storage
- **Data Protection**: Encrypt sensitive test data and credentials
- **Base64 Encoding**: Safe text encoding for encrypted data

### Usage
```gherkin
Scenario: Encryption Operations
  # Encrypt sensitive data
  * def encryptedData = encryptionService.encrypt('sensitive-password', 'encryption-key')
  * print 'Encrypted:', encryptedData
  
  # Decrypt data
  * def decryptedData = encryptionService.decrypt(encryptedData, 'encryption-key')
  * match decryptedData == 'sensitive-password'
  
  # Generate secure key
  * def secureKey = encryptionService.generateKey()
```

### Key Files
- `utilities/EncryptionService.java` - Core encryption functionality
- `utilities/projectUtilities/features/encryption-test.feature` - Usage examples

---

## 📁 SFTP Operations

### Features
- **Secure File Transfer**: SFTP protocol support
- **Batch Operations**: Upload/download multiple files
- **Directory Management**: Create, list, and manage remote directories
- **Authentication**: Password and key-based authentication
- **Progress Tracking**: File transfer progress monitoring

### Usage
```gherkin
Scenario: SFTP File Operations
  # Connect to SFTP server
  * sftpService.connect('hostname', 22, 'username', 'password')
  
  # Upload file
  * def uploadResult = sftpService.uploadFile('/local/path/file.txt', '/remote/path/file.txt')
  * match uploadResult.success == true
  
  # Download file
  * def downloadResult = sftpService.downloadFile('/remote/path/file.txt', '/local/download/file.txt')
  
  # List directory contents
  * def fileList = sftpService.listFiles('/remote/directory')
  
  # Disconnect
  * sftpService.disconnect()
```

### Key Files
- `utilities/SftpService.java` - SFTP operations
- `utilities/SecureTransferService.java` - Enhanced transfer service
- `utilities/projectUtilities/features/sftp-transfer-test.feature` - Examples

---

## 🔑 Okta Authentication

### Features
- **OAuth 2.0**: Standard OAuth 2.0 token flow
- **Token Management**: Automatic token refresh and caching
- **Multi-Environment**: Support for different Okta environments
- **Retry Logic**: Configurable retry for authentication failures
- **Secure Storage**: Encrypted token storage

### Configuration
```json
{
  "okta": {
    "domain": "your-domain.okta.com",
    "clientId": "your-client-id",
    "clientSecret": "your-client-secret",
    "scope": "openid profile email",
    "grantType": "client_credentials"
  }
}
```

### Usage
```gherkin
Scenario: Okta Authentication
  # Get access token
  * def tokenResponse = oktaService.getAccessToken()
  * match tokenResponse.access_token != null
  
  # Use token in API calls
  * def headers = { Authorization: 'Bearer ' + tokenResponse.access_token }
  * def response = call read('api-call.feature') { headers: headers }
  
  # Refresh token if needed
  * def refreshedToken = oktaService.refreshToken(tokenResponse.refresh_token)
```

### Key Files
- `utilities/OktaTokenService.java` - Okta integration
- `utilities/projectOkta/features/okta-token-flow.feature` - Authentication flows

---

## 📊 Allure Reporting

### Features
- **Rich Reports**: Detailed HTML reports with attachments
- **Test Attachments**: Automatic logging of requests, responses, and data
- **Performance Metrics**: Execution time tracking
- **Error Details**: Comprehensive error reporting with stack traces
- **Integration**: Seamless integration with Karate tests

### Automatic Attachments
- Database query details and results
- API request/response data
- Test execution context
- Performance timing information
- Error details and stack traces

### Usage in Features
```gherkin
* def allureHelper = read('classpath:utilities/allure-helper.js')
* call allureHelper.addTestStep('API Call', 'Making GET request to /users')
* call allureHelper.addJsonAttachment('Response Data', response)
* call allureHelper.addPerformanceInfo('API Call', startTime)
```

### Key Files
- `utilities/AllureKarateHook.java` - Allure integration
- `utilities/AllureAttachmentHelper.java` - Attachment utilities
- `utilities/allure-helper.js` - JavaScript helpers for features

---

## 📁 Project Structure

```
ExaAPI-master/
├── src/test/java/
│   ├── utilities/                    # Core utilities
│   │   ├── projectSQL/              # Database testing
│   │   ├── projectNotifications/    # Notification testing
│   │   ├── projectUtilities/        # Utility testing
│   │   ├── KarateDatabaseService.java
│   │   ├── EmailNotificationService.java
│   │   ├── EncryptionService.java
│   │   ├── SftpService.java
│   │   ├── OktaTokenService.java
│   │   └── AllureAttachmentHelper.java
│   ├── projects/                    # Individual projects
│   │   ├── projectA/
│   │   ├── projectAPI/
│   │   └── projectOkta/
│   └── karate-config.js            # Global configuration
├── target/
│   ├── karate-reports/             # Karate HTML reports
│   ├── allure-report/              # Allure HTML reports
│   └── report-archives/            # Zipped reports
├── run-tests-with-allure.bat       # Test execution script
└── pom.xml                         # Maven configuration
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL (for database testing)
- SFTP server access (for SFTP testing)
- Okta developer account (for authentication testing)

### Installation
1. Clone the repository
2. Configure database connections in project configs
3. Set up notification webhooks
4. Configure Okta credentials
5. Run tests: `./run-tests-with-allure.bat`

### Running Tests
```bash
# Run specific project
mvn test -Dtest=utilities.projectSQL.ProjectSQL_Runner -Dproject=projectSQL

# Run with Allure reports
./run-tests-with-allure.bat

# Generate Allure report only
mvn allure:report
```

### Configuration Files
- `utilities/projectSQL/projectSQL-config.json` - Database settings
- `utilities/projectNotifications/projectNotifications-config.json` - Notification settings
- `utilities/projectUtilities/projectUtilities-config.json` - Utility settings

---

## 🔧 Key Features Summary

✅ **Database Testing**: PostgreSQL integration with JDBC  
✅ **Multi-Channel Notifications**: Email, Slack, Teams  
✅ **Security**: AES encryption and decryption  
✅ **File Transfer**: Secure SFTP operations  
✅ **Authentication**: Okta OAuth 2.0 integration  
✅ **Rich Reporting**: Allure reports with attachments  
✅ **Automated Workflows**: Test execution with notifications  
✅ **Retry Logic**: Configurable retry mechanisms  
✅ **Report Archives**: Zipped reports via email  
✅ **Performance Tracking**: Execution time monitoring  

---

*For detailed examples and advanced usage, refer to the feature files in each project directory.*
