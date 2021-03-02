/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.undergroundcreative.QuickSDK;

import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.*;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;
import com.quicksdk.Extend;

public class QuickSDKPluginMainActivity extends CordovaActivity {

    static CallbackContext callbackContext;
    private String productCode;
    private String productKey;

    public static void setCallbackContext(CallbackContext c){
        callbackContext = c;
    }

    private String getProductCode() {
        return productCode;
    }

    private void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    private String getProductKey() {
        return productKey;
    }

    private void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("QuickSDKPluginMainActivity", "onCreate");

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            Log.d("appInfo", appInfo.toString());
            Log.d("bundle", bundle.toString());
            setProductCode(bundle.getString("com.undergroundcreative.QuickSDK.productCode"));
            setProductKey(bundle.getString("com.undergroundcreative.QuickSDK.productKey"));
            if(getProductCode() != null) {
                Log.d("productCode", getProductCode());
            }
            if(getProductKey() != null) {
                Log.d("productKey", getProductKey());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading productCode and productKey - " + e);
        }
        this.setProductCode(getProductCode());
        this.setProductKey(getProductKey());

        try {
            // check permissions
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                // Not got permissions - request them
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            } else {
                // If yes, perform initialization
                //// Set notifications to monitor the return value of initialization, login, logout, payment and logout functions (required)
                initQkNotifiers();
                Sdk.getInstance().init(this, this.getProductCode(), this.getProductKey());
            }
        } catch (Exception e) {
            // Error - try again
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }

        com.quicksdk.Sdk.getInstance().onCreate(this);
    }
    //Callback for requesting permission (result)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Success
            initQkNotifiers();
            Sdk.getInstance().init(this, this.getProductCode(), this.getProductKey());
        } else {
            //Failure The logic here is based on the game. This is just a simulation of application failure. Exit the game and continue to apply or other logic.
            Sdk.getInstance().init(this, this.getProductCode(), this.getProductKey());
            System.exit(0);
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        com.quicksdk.Sdk.getInstance().onStart(this);
        Log.d("MainActivity", "onStart");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        com.quicksdk.Sdk.getInstance().onRestart(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        com.quicksdk.Sdk.getInstance().onPause(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        com.quicksdk.Sdk.getInstance().onResume(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        com.quicksdk.Sdk.getInstance().onStop(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        com.quicksdk.Sdk.getInstance().onDestroy(this);
    }
    //@Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
        com.quicksdk.Sdk.getInstance().onNewIntent(intent);
    }
    //@Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        com.quicksdk.Sdk.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    public void setUserInfo() {
        Log.d("quicksdk","setUserInfo 1");
        long unixTime = System.currentTimeMillis() / 1000L;
        String RoleCreateTime = String.valueOf(unixTime);
        Log.d("RoleCreateTime",RoleCreateTime);
        GameRoleInfo roleInfo = new GameRoleInfo();
        roleInfo.setServerID("1");// Server id
        roleInfo.setServerName("火星服务器");// Mars server
        roleInfo.setGameRoleName("Football Chairman Test");//
        roleInfo.setGameRoleID("0");// Role ID
        roleInfo.setGameUserLevel("0");// Set game character level
        roleInfo.setVipLevel("0"); // Set the current user's VIP level, it must be a numeric integer string, do not pass similar strings such as "vip1"
        roleInfo.setGameBalance("50000");// Role's existing amount
        roleInfo.setPartyName("Default"); // Set gang, guild name
        roleInfo.setRoleCreateTime(RoleCreateTime); // Required for UC and 1881 channels, the value is a 10-digit time stamp

        roleInfo.setPartyId("0"); // 360 channel parameter, set the gang id, must be an integer string
        roleInfo.setGameRoleGender("Male"); // 360 channel parameters
        roleInfo.setGameRolePower("0"); // 360 channel parameters, set the character combat power, must be an integer string
        roleInfo.setPartyRoleId("0"); // 360 channel parameters, set the id of the role in the gang
        roleInfo.setPartyRoleName("Chairman"); // 360 channel parameters, set the name of the role in the gang
        roleInfo.setProfessionId("0"); // 360 channel parameters, set the role occupation id, must be an integer string
        roleInfo.setProfession("Chairman"); // 360 channel parameters, set the role occupation name
        roleInfo.setFriendlist("No"); // 360 channel parameters, set the friend relationship list, please refer to the format：http://open.quicksdk.net/help/detail/aid/190

        com.quicksdk.User.getInstance().setGameRoleInfo(this, roleInfo, true);
        Log.d("quicksdk","setUserInfo 2");
    }


    private void initQkNotifiers() {
        QuickSDK.getInstance()
                // 1. Set initialization notification (required)
                .setInitNotifier(new InitNotifier() {

                    @Override
                    public void onSuccess() {
                        Log.d("MainActivity", "\n" + "initialized successfully");
                    }

                    @Override
                    public void onFailed(String message, String trace) {
                        Log.d("MainActivity", "initialized failed:" + message);
                    }
                })
                // 2. Set login notification (required)
                .setLoginNotifier(new LoginNotifier() {

                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            Log.d("MainActivity", "Login success!" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName() + "\n\r"
                                    + "Token:  " + userInfo.getToken());

                            int channelID = Extend.getInstance().getChannelType();
                            Log.d("channelID", String.valueOf(channelID));

                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("result", "Successfully logged in");
                                obj.put("UserName", userInfo.getUserName());
                                obj.put("channelID", channelID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("MainActivity", "JSON obj: " + obj.toString());
                            Log.d("login callbackContext", callbackContext.toString());
                            callbackContext.success(obj);
                            Log.d("MainActivity", "about to setUserInfo");
                            // After logging in successfully, when entering the game, you need to submit user information to the channel
                            setUserInfo();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.d("MainActivity", "Login Cancelled");
                        callbackContext.error("Login Cancelled");
                    }

                    @Override
                    public void onFailed(final String message, String trace) {
                        Log.d("MainActivity", "Login failed:" + message);
                        callbackContext.error("Login failed:" + message);
                    }

                })
                // 3. Set logout notification (required)
                .setLogoutNotifier(new LogoutNotifier() {

                    @Override
                    public void onSuccess() {
                        Log.d("MainActivity", "Logout successful");
                        Log.d("logout callbackContext", callbackContext.toString());
                        callbackContext.success("Logout successful");
                    }

                    @Override
                    public void onFailed(String message, String trace) {
                        Log.d("MainActivity", "Logout failed:" + message);
                        callbackContext.error("Logout failed:" + message);
                    }
                })
                // 4. Set up switch account notification (required)
                .setSwitchAccountNotifier(new SwitchAccountNotifier() {

                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            Log.d("MainActivity", "Switched account successfully" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName() + "\n\r"
                                    + "Token:  " + userInfo.getToken());
                            // SW added 26/2/21 - don't know if need to do this? See 2.6.4 of doc-13
                            setUserInfo();
                        }
                    }

                    @Override
                    public void onFailed(String message, String trace) {
                        Log.d("MainActivity", "Failed to switch account:" + message);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("MainActivity", "Cancelled account switching");
                    }
                })
                // 5.Set payment notification (required)
                .setPayNotifier(new PayNotifier() {

                    @Override
                    public void onSuccess(String sdkOrderID, String cpOrderID, String extrasParams) {
                        Log.d("MainActivity", "payment successful，sdkOrderID:" + sdkOrderID + ",cpOrderID:" + cpOrderID);
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("result", "Successfully purchased");
                            obj.put("productId", extrasParams);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("pay callbackContext", callbackContext.toString());
                        callbackContext.success(obj);
                    }

                    @Override
                    public void onCancel(String cpOrderID) {
                        Log.d("MainActivity", "Payment cancellation，cpOrderID:" + cpOrderID);
                        callbackContext.error("Payment cancelled");
                    }

                    @Override
                    public void onFailed(String cpOrderID, String message, String trace) {
                        Log.d("MainActivity", "Payment failed:" + "pay failed,cpOrderID:" + cpOrderID + ",message:" + message);
                        callbackContext.error("Payment failed");
                    }
                })
                // 6. Set exit notification (required)
                .setExitNotifier(new ExitNotifier() {

                    @Override
                    public void onSuccess() {
                        Log.d("MainActivity", "about to finish");
                        callbackContext.success("Exit success");
                        // The exit operation of the game itself, the finish() below is just an example
                        finish();
                    }

                    @Override
                    public void onFailed(String message, String trace) {
                        Log.d("MainActivity", "Exit failed：" + message);
                        callbackContext.error("Exit failed");
                    }
                });
    }
}
