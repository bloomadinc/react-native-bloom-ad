package cn.bloomad;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.linkin.adsdk.AdConfig;
import com.linkin.adsdk.AdSdk;

import java.util.HashMap;
import java.util.Map;

import cn.bloomad.module.InterstitialModule;
import cn.bloomad.module.ModuleManager;
import cn.bloomad.module.RewardVideoModule;
import cn.bloomad.module.SplashModule;

public class BloomAdModule extends ReactContextBaseJavaModule {
    private static final String TAG = BloomAdModule.class.getSimpleName();
    private final ReactApplicationContext reactContext;
    private ModuleManager moduleManager;

    public static Activity mActivity;


    public BloomAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        moduleManager = ModuleManager.getInstance();
    }

    @Override
    public String getName() {
        return "BloomAd";
    }

    @ReactMethod
    public void init(String appId, Promise promise) {
        try {
            AdSdk.getInstance().init(reactContext,
                    new AdConfig.Builder().appId(appId).multiProcess(false).debug(BuildConfig.DEBUG).build(), null);
            promise.resolve(appId);
        } catch (IllegalViewOperationException e) {
            promise.reject(e);
        }

    }

    @ReactMethod
    public void setUserId(String userId, Promise promise) {
        try {
            if (userId.length() > 0) {
                AdSdk.getInstance().setUserId(userId);
            } else {
                AdSdk.getInstance().setUserId(null);
            }
            promise.resolve(userId);
        } catch (IllegalViewOperationException e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void rewardVideo(String name, String unitId) {
        mActivity = getCurrentActivity();
        RewardVideoModule rewardVideoModule = new RewardVideoModule(reactContext, mActivity, name);
        Map<String, String> map = new HashMap<String, String>();
        map.put("unitId",unitId);
        rewardVideoModule.action(map);
    }

    @ReactMethod
    public void interstitial(String name, float width, String unitId) {
        mActivity = getCurrentActivity();
        InterstitialModule interstitialModule = new InterstitialModule(reactContext, mActivity, name);
        Map<String, String> map = new HashMap<String, String>();
        map.put("width", Float.toString(width));
        map.put("unitId",unitId);
        interstitialModule.action(map);
    }

    @ReactMethod
    public void destroyView(String name) {
        moduleManager.remove(name);
    }


    @ReactMethod
    public void showSplash( String name,  int interval, String unitId) {
        mActivity = getCurrentActivity();
        Log.d(TAG, "showSplash:" + unitId);
        SplashModule splashModule = new SplashModule(reactContext, mActivity, name);
        splashModule.show(interval, unitId);
    }


}
