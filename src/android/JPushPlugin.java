package cn.jiguang.cordova.push;

import android.content.Context;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;

public class JPushPlugin extends CordovaPlugin {
    private static final String TAG = JPushPlugin.class.getSimpleName();

    private Context mContext;
    private static CallbackContext mCallback;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mContext = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callback)
            throws JSONException {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = JPushPlugin.class.getDeclaredMethod(action,
                            JSONArray.class, CallbackContext.class);
                    method.invoke(JPushPlugin.this, args, callback);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        return true;
    }

    void init(JSONArray args, CallbackContext callback) {
        JPushInterface.init(mContext);
        mCallback = callback;
    }

    void setDebugMode(JSONArray args, CallbackContext callback) throws JSONException {
        JSONObject params = args.getJSONObject(0);
        boolean enable = params.getBoolean("enable");
        JPushInterface.setDebugMode(enable);
    }

    void getRegistrationId(JSONArray args, CallbackContext callback) {
        String rId = JPushInterface.getRegistrationID(mContext);
        callback.success(rId);
    }

    void stopPush(JSONArray args, CallbackContext callback) {
        JPushInterface.stopPush(mContext);
    }

    void resumePush(JSONArray args, CallbackContext callback) {
        JPushInterface.resumePush(mContext);
    }

    void isPushStopeed(JSONArray args, CallbackContext callback) {
        boolean isPushStopped = JPushInterface.isPushStopped(mContext);
        callback.success(String.valueOf(isPushStopped));
    }

    static void onReceiveRegistrationId(String rId) throws JSONException {
        JSONObject value = new JSONObject();
        value.put("registrationId", rId);

        JSONObject event = new JSONObject();
        event.put("receiveRegistrationId", value);

        eventSuccess(event);
    }

    static void onReceiveNotification(JSONObject notification) throws JSONException {
        JSONObject event = new JSONObject();
        event.put("receiveNotification", notification);

        eventSuccess(event);
    }

    static void onOpenNotification(JSONObject notification) throws JSONException {
        JSONObject event = new JSONObject();
        event.put("openNotification", notification);

        eventSuccess(event);
    }

    static void onReceiveMessage(JSONObject message) throws JSONException {
        JSONObject event = new JSONObject();
        event.put("receiveMessage", message);

        eventSuccess(event);
    }

    static void onConnectionChange(boolean isConnected) throws JSONException {
        JSONObject value = new JSONObject();
        value.put("isConnected", isConnected);

        JSONObject event = new JSONObject();
        event.put("connectionChange", value);

        eventSuccess(event);
    }

    private static void eventSuccess(JSONObject value) {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, value);
        pluginResult.setKeepCallback(true);
        mCallback.sendPluginResult(pluginResult);
    }
}
