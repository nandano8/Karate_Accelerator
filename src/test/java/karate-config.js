function fn(){
    var env = karate.env // get system property 'karate.env'
    var project = karate.properties['project'] //get system property 'karate.project'

    karate.log("karate.env system property was:", env)
    karate.log("karate.project system property was:", project)
    karate.configure("ssl",  {trustAll: true});

    if(!env){
        env = "qa";
        karate.log("As env is not specified using ", env);
    }

    var configFile = 'classpath:projects/'+project+'/'+project+'-config.json';
    var configData = karate.read(configFile);

    var envConfig = configData.environments[env]? configData.environments[env]:'';
    var config = envConfig;
    
    // Add database service
    config.db = Java.type('utilities.KarateDatabaseService');
    
    // Add Okta token service
    config.oktaService = Java.type('utilities.OktaTokenService');
    
    // Add utility services
    config.encryptionService = Java.type('utilities.EncryptionService');
    config.sftpService = Java.type('utilities.SftpService');
    config.secureTransferService = Java.type('utilities.SecureTransferService');
    
    // Add notification services
    config.emailService = Java.type('utilities.EmailNotificationService');
    config.slackService = Java.type('utilities.SlackNotificationService');
    config.notificationService = Java.type('utilities.NotificationService');
    
    // Add retry service
    config.retryService = Java.type('utilities.RetryService');
    
    // Add test lifecycle hooks
    config.testHook = Java.type('utilities.TestLifecycleHook');
    config.testRunner = Java.type('utilities.TestNotificationRunner');

    karate.log(config);
    return config;
}