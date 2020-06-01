package cn.bloomad;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.linkin.adsdk.AdSdk;
import java.util.HashMap;
import java.util.Map;

import cn.bloomad.module.InitModule;
import cn.bloomad.module.InterstitialModule;
import cn.bloomad.module.ModuleManager;
import cn.bloomad.module.RewardVideoModule;
import cn.bloomad.module.SplashModule;

public class BloomAdModule extends ReactContextBaseJavaModule {
    private static final String TAG = BloomAdModule.class.getSimpleName();
    private final ReactApplicationContext reactContext;
    private ModuleManager moduleManager;

    public static Activity mActivity;

    private InitModule initModule;


    public BloomAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        moduleManager = ModuleManager.getInstance();

        initModule = InitModule.getInstance();
    }

    @Override
    public String getName() {
        return "BloomAd";
    }

    @ReactMethod
    public void init(String appId, Promise promise) {
        try {
            mActivity = getCurrentActivity();
            // AdSdk 在 VideoSdk 之前初始化，视频流中才能展现广告
            initModule.init(mActivity, appId);
            promise.resolve(appId);
        } catch (IllegalViewOperationException e) {
            promise.reject(e);
        }

    }

    @ReactMethod
    public void setUserId(String userId, Promise promise) {
        try {
            if (userId != null && userId.length() > 0) {
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
    public void rewardVideo(String name, String unitId, Boolean showWhenCached) {
        mActivity = getCurrentActivity();
        RewardVideoModule rewardVideoModule = new RewardVideoModule(reactContext, mActivity, name);
        Map<String, String> map = new HashMap<String, String>();
        map.put("unitId",unitId);
        map.put("showWhenCached", showWhenCached ? "1" : "0");
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
