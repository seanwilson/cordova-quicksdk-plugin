package com.undergroundcreative.QuickSDK;

import android.content.Context;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.User;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;
import com.undergroundcreative.QuickSDK.QuickSDKPluginMainActivity;

import java.util.UUID;
import java.lang.String;

// https://github.com/TooTallNate/Java-WebSocket
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class QuickSDKPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
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
        } else if (action.equals("exit")) {
            this.exit(callbackContext);
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
        Log.d("login callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
        com.quicksdk.User.getInstance().login(this.cordova.getActivity());
    }

    public void logout(CallbackContext callbackContext) {
        Log.d("quicksdk","logout");
        Log.d("logout callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
        com.quicksdk.User.getInstance().logout(this.cordova.getActivity());
    }

    private void pay(String name, String id, Double price, CallbackContext callbackContext) {

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

        Log.d("pay callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);

        Log.d("QuickSDKPlugin", "Create socket");

        URI uri;
        try {
            uri = new URI(String.format("ws://m1.fengkuangtiyu.cn/footballc/%s", orderInfo.getCpOrderID()));
            Log.d("QuickSDKPlugin", uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        WebSocketClient mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                com.quicksdk.Payment.getInstance().pay(cordova.getActivity(), orderInfo, roleInfo);
            }

            @Override
            public void onMessage(String message) {
                Log.d("Websocket", "Received OnMessage! " + message);
                try {
                    JSONObject mainObject = new JSONObject(message);
                    JSONObject msgObject = mainObject.getJSONObject("message");
                    String status = msgObject.getString("status");
                    String extras_params = msgObject.getString("extras_params");
                    Log.i("status", status);

                    if (status.equals("0")){
                        Log.d("QuickSDKPlugin", "About to give IAP! ");
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("result", "Successfully purchased");
                            obj.put("productId", extras_params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("pay callbackContext", callbackContext.toString());
                        callbackContext.success(obj);
                    } else {
                        callbackContext.error("Payment error: " + status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callbackContext.error("Payment error: " + e.getMessage());
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
                callbackContext.error("Payment error: " + e.getMessage());
            }
        };
        mWebSocketClient.connect();

    }

    public void exit(CallbackContext callbackContext) {
        Log.d("quicksdk","exit");
        Log.d("exit callbackContext", callbackContext.toString());
        QuickSDKPluginMainActivity.setCallbackContext(callbackContext);
        Sdk.getInstance().exit(this.cordova.getActivity());
    }
}