cordova.define("com-undergroundcreative-quicksdk.QuickSDKPlugin", function(require, exports, module) {

        var argscheck = require('cordova/argscheck');
        var channel = require('cordova/channel');
        var exec = require('cordova/exec');
        var cordova = require('cordova');

        //channel.createSticky('onCordovaInfoReady');
        // Tell cordova channel to wait on the CordovaInfoReady event
        //channel.waitForInitialization('onCordovaInfoReady');

        function QuickSDKPlugin () {
            this.available = false;
            this.platform = null;
            this.version = null;
            this.uuid = null;
            this.cordova = null;
            this.model = null;
            this.manufacturer = null;
            this.isVirtual = null;
            this.serial = null;

            var me = this;

            channel.onCordovaReady.subscribe(function () {
                console.log("JS QuickSDKPlugin 1");
            });
        }

        QuickSDKPlugin.prototype.helloWorld = function (arg0, successCallback, errorCallback) {
            //argscheck.checkArgs('fF', 'QuickSDKPlugin.getInfo', arguments);
            console.log("JS helloWorld 1");
            exec(successCallback, errorCallback, 'QuickSDKPlugin', 'echo', [arg0]);
            console.log("JS helloWorld 2");
        };

        QuickSDKPlugin.prototype.initialise = function (successCallback, errorCallback) {
            console.log("JS QuickSDKPlugin initialise 1");
            exec(successCallback, errorCallback, 'QuickSDKPlugin', 'initialise');
            console.log("JS QuickSDKPlugin initialise 2");
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

        QuickSDKPlugin.prototype.testMethod = function (successCallback, errorCallback) {
            console.log("JS QuickSDKPlugin testMethod 1");
            exec(successCallback, errorCallback, 'QuickSDKPlugin', 'testMethod');
            console.log("JS QuickSDKPlugin testMethod 2");
        };

        module.exports = new QuickSDKPlugin();



});
