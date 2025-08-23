function() {
    var DatabaseHelper = Java.type('utilities.DatabaseHelper');
    
    return {
        connect: function(url, username, password) {
            try {
                DatabaseHelper.connect(url, username, password);
                return { success: true };
            } catch (e) {
                return { success: false, error: e.message };
            }
        },
        
        select: function(sql, params) {
            try {
                var args = params || [];
                return DatabaseHelper.select(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        insert: function(sql, params) {
            try {
                var args = params || [];
                return DatabaseHelper.insert(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        update: function(sql, params) {
            try {
                var args = params || [];
                return DatabaseHelper.update(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        delete: function(sql, params) {
            try {
                var args = params || [];
                return DatabaseHelper.delete(sql, args);
            } catch (e) {
                throw new Error('SQL Error: ' + e.message);
            }
        },
        
        disconnect: function() {
            try {
                DatabaseHelper.disconnect();
                return { success: true };
            } catch (e) {
                return { success: false, error: e.message };
            }
        },
        
        isConnected: function() {
            try {
                return DatabaseHelper.isConnected();
            } catch (e) {
                return false;
            }
        }
    };
}