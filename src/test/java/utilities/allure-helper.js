// Karate JavaScript helper for Allure attachments
function addTextAttachment(name, content) {
    var AllureAttachmentHelper = Java.type('utilities.AllureAttachmentHelper');
    AllureAttachmentHelper.addTextAttachment(name, content);
}

function addJsonAttachment(name, jsonContent) {
    var AllureAttachmentHelper = Java.type('utilities.AllureAttachmentHelper');
    var jsonString = typeof jsonContent === 'string' ? jsonContent : JSON.stringify(jsonContent, null, 2);
    AllureAttachmentHelper.addJsonAttachment(name, jsonString);
}

function addApiDetails(method, url, requestBody, responseBody, statusCode) {
    var AllureAttachmentHelper = Java.type('utilities.AllureAttachmentHelper');
    var reqBody = requestBody ? JSON.stringify(requestBody, null, 2) : null;
    var resBody = responseBody ? JSON.stringify(responseBody, null, 2) : null;
    AllureAttachmentHelper.addRequestDetails(method, url, reqBody, resBody, statusCode);
}

function addTestStep(stepName, details) {
    var AllureAttachmentHelper = Java.type('utilities.AllureAttachmentHelper');
    var content = '=== Test Step ===\n' +
                  'Step: ' + stepName + '\n' +
                  'Details: ' + details + '\n' +
                  'Timestamp: ' + new Date().toISOString();
    AllureAttachmentHelper.addTextAttachment('Test Step: ' + stepName, content);
}

function addPerformanceInfo(operation, startTime) {
    var AllureAttachmentHelper = Java.type('utilities.AllureAttachmentHelper');
    var duration = Date.now() - startTime;
    AllureAttachmentHelper.addPerformanceMetrics(operation, duration, true);
}