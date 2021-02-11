try{
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
                console.log("JS quickSDK 1");
            });
        }
        
        QuickSDKPlugin.prototype.helloWorld = function (arg0, successCallback, errorCallback) {
            //argscheck.checkArgs('fF', 'QuickSDKPlugin.getInfo', arguments);
            console.log("JS helloWorld 1");
            exec(successCallback, errorCallback, 'QuickSDKPlugin', 'echo', [arg0]);
            console.log("JS helloWorld 2");
        };
        
        module.exports = new QuickSDKPlugin();
        
    });
}
catch(e){
    console.log("Error defining plugin: " + JSON.stringify(e));
}