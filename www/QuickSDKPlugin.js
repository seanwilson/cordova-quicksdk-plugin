var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var exec = require('cordova/exec');
var cordova = require('cordova');

//channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
//channel.waitForInitialization('onCordovaInfoReady');

function QuickSDKPlugin () {
    channel.onCordovaReady.subscribe(function () {
        console.log("JS QuickSDKPlugin 1");
    });
}

QuickSDKPlugin.prototype.isLoggedin = function (successCallback, errorCallback) {
    console.log("JS QuickSDKPlugin isLoggedin 1");
    exec(successCallback, errorCallback, 'QuickSDKPlugin', 'isLoggedin');
    console.log("JS QuickSDKPlugin isLoggedin 2");
};

QuickSDKPlugin.prototype.login = function (successCallback, errorCallback) {
    console.log("JS QuickSDKPlugin login 1");
    exec(successCallback, errorCallback, 'QuickSDKPlugin', 'login');
    console.log("JS QuickSDKPlugin login 2");
};

QuickSDKPlugin.prototype.logout = function (successCallback, errorCallback) {
    console.log("JS QuickSDKPlugin logout 1");
    exec(successCallback, errorCallback, 'QuickSDKPlugin', 'logout');
    console.log("JS QuickSDKPlugin logout 2");
};

QuickSDKPlugin.prototype.pay = function (successCallback, errorCallback, name, id, price) {
    console.log("JS QuickSDKPlugin pay 1");
    exec(successCallback, errorCallback, 'QuickSDKPlugin', 'pay', [name, id, price]);
    console.log("JS QuickSDKPlugin pay 2");
};

QuickSDKPlugin.prototype.exit = function (successCallback, errorCallback) {
    console.log("JS QuickSDKPlugin exit 1");
    exec(successCallback, errorCallback, 'QuickSDKPlugin', 'exit');
    console.log("JS QuickSDKPlugin exit 2");
};

module.exports = new QuickSDKPlugin();