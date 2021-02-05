var exec = require('cordova/exec');

exports.helloWorld = function (arg0, success, error) {
    exec(success, error, 'QuickSDK', 'helloWorld', [arg0]);
};

exports.echo = function(str, callback) {
    cordova.exec(callback, function(err) {
        callback('Nothing to echo.');
    }, "Echo", "echo", [str]);
};