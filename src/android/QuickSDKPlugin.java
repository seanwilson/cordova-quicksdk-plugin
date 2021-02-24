package com.undergroundcreative.QuickSDK;

/*import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;*/
import android.content.Context;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
/*import org.json.JSONObject;

import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;*/
import com.quicksdk.User;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;
/*import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;*/
//import com.undergroundcreative.quicksdktest3.MainActivity;
import com.undergroundcreative.QuickSDK.QuickSDKPluginMainActivity;

import java.util.UUID;

/**
* This class echoes a string called from JavaScript.
*/
public class QuickSDKPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // your init code here
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        final CordovaPlugin plugin = (CordovaPlugin) this;
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        } else if (action.equals("isLoggedin")) {
            this.isLoggedin(callbackContext);
            return true;
        } else if (action.equals("login")) {
            this.login(callbackContext);
            return true;
            /*cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    Log.d("quicksdk","login");
                    Log.d("about to set login callbackContext", callbackContext.toString());
                    QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
                    com.quicksdk.User.getInstance().login(plugin.cordova.getActivity());
                }
            });
            return true;*/
        } else if (action.equals("logout")) {
            this.logout(callbackContext);
            return true;
        } else if (action.equals("pay")) {
            String name = args.getString(0);
            String id = args.getString(1);
            Double price = args.getDouble(2);
            this.pay(name, id, price, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public void isLoggedin(CallbackContext callbackContext) {
        Log.d("quicksdk","isLoggedin");
        try {
            QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
            boolean isLoggedin = User.getInstance().isLogin(this.cordova.getActivity());
            Log.d("quicksdk isLoggedin", String.valueOf(isLoggedin));
            callbackContext.success(String.valueOf(isLoggedin));
        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    public void login(CallbackContext callbackContext) {
        Log.d("quicksdk","login");
        Log.d("about to set login callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
        com.quicksdk.User.getInstance().login(this.cordova.getActivity());
    }

    public void logout(CallbackContext callbackContext) {
        Log.d("quicksdk","logout");
        Log.d("about to set logout callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
        com.quicksdk.User.getInstance().logout(this.cordova.getActivity());
    }

    private void pay(String name, String id, Double price, CallbackContext callbackContext) {

        // example of how to use callback to js
        //callbackContext.success("Payment succeeded and shit");
        //callbackContext.error("Payment failed");
        Log.d("QuickSDKPlugin","name " + name);
        Log.d("QuickSDKPlugin","id " + id);
        Log.d("QuickSDKPlugin","price " + price);
        long unixTime = System.currentTimeMillis() / 1000L;
        String RoleCreateTime = String.valueOf(unixTime);
        Log.d("RoleCreateTime",RoleCreateTime);

        GameRoleInfo roleInfo = new GameRoleInfo();
        roleInfo.setServerID("1");// Server ID, its value must be a numeric string
        roleInfo.setServerName("火星服务器");  // Mars server
        roleInfo.setGameRoleName("Football Chairman Test");
        roleInfo.setGameRoleID("0");// Role ID
        roleInfo.setGameUserLevel("0");// Set game character level
        roleInfo.setVipLevel("0");// Set the current user's VIP level, it must be a numeric integer string, do not pass similar strings such as "vip1"
        roleInfo.setGameBalance("50000");// Role's existing amount
        roleInfo.setPartyName("Default");// Set gang, guild name
        roleInfo.setRoleCreateTime(RoleCreateTime); // Required for UC and 1881 channels, the value is a 10-digit time stamp

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCpOrderID(UUID.randomUUID().toString().replace("-", ""));// Game order number
        orderInfo.setGoodsName(name);// product name
        orderInfo.setCount(1);// Purchase quantity, if you buy "10 Yuanbao", pass 10
        orderInfo.setAmount(price); //Total amount (unit: yuan)
        orderInfo.setGoodsID(id); //Product ID, used to identify the purchased product
        orderInfo.setGoodsDesc(id);
        orderInfo.setExtrasParams(id); // Transparent transmission parameters

        Log.d("about to set pay callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
        com.quicksdk.Payment.getInstance().pay(this.cordova.getActivity(), orderInfo, roleInfo);
    }
}