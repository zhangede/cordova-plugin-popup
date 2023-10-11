var exec = require('cordova/exec');
var PLUGIN_NAME = 'popup';

var popup = {

	message : function (messages, success, error ) {
		exec(success, error, PLUGIN_NAME, 'message', messages); //messages ist ein array
	},
	test: function (param,success, error ) {
		exec(success, error, PLUGIN_NAME, 'test', [param]); //param ist nur ein objekt 
	},
	error: function (success, error ) {
		exec(success, error, PLUGIN_NAME, 'error', []);
	}
};

module.exports = popup;