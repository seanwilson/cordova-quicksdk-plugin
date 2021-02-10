#!/usr/bin/env node

var APP_CLASS = 'com.undergroundcreative.QuickSDK.MyApplication';

module.exports = function(context) {

  //var fs = context.requireCordovaModule('fs'),
     // path = context.requireCordovaModule('path');
      var fs = require('fs');
      var path = require('path');
      

  var platformRoot = path.join(context.opts.projectRoot, 'platforms/android');
  var manifestFile = path.join(platformRoot, 'AndroidManifest.xml');

  console.log("android_app_name 1");

  if (fs.existsSync(manifestFile)) {
    console.log("android_app_name 2");
    fs.readFile(manifestFile, 'utf8', function (err, data) {
      console.log("android_app_name 3");
      if (err) {
        throw new Error('Unable to find AndroidManifest.xml: ' + err);
      }
      console.log("android_app_name 4");

      if (data.indexOf(APP_CLASS) == -1) {
        console.log("android_app_name 5");
        var result = data.replace(/<application/g, '<application android:name="' + APP_CLASS + '"');
        console.log("android_app_name 6");
        fs.writeFile(manifestFile, result, 'utf8', function (err) {
          console.log("android_app_name 7");
          if (err) throw new Error('Unable to write AndroidManifest.xml: ' + err);
        })
        console.log("android_app_name 8");
      }
    });
    console.log("android_app_name 9");
  }
  console.log("android_app_name 10");
  console.log("android_app_name 11");

};