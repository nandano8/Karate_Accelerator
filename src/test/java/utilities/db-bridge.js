function() {
    var KarateDatabaseService = Java.type('utilities.KarateDatabaseService');
    
    return {
        connect: function(url, username, password) {
            try {
                KarateDatabaseService.connect(url, username, password);
                return { success: true };
            } catch (e) {
                return { success: false, error: e.message };
            }
        },
        
        select: function(sql, params) {
            try {
                var args = params || [];
                return KarateDatabaseService.select(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        insert: function(sql, params) {
            try {
                var args = params || [];
                return KarateDatabaseService.insert(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        update: function(sql, params) {
            try {
                var args = params || [];
                return KarateDatabaseService.update(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        delete: function(sql, params) {
            try {
                var args = params || [];
                return KarateDatabaseService.delete(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        disconnect: function() {
            try {
                KarateDatabaseService.disconnect();
                return { success: true };
            } catch (e) {
                return { success: false, error: e.message };
            }
        },
        
        isConnected: function() {
            try {
                return KarateDatabaseService.isConnected();
            } catch (e) {
                return false;
            }
        }
    };
}