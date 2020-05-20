package cn.bloomad.module;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.linkin.adsdk.AdSdk;

import java.util.Map;

public class InterstitialModule extends EventModule {

    public InterstitialModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
    }

    @Override
    public void threadAction(Activity mActivity, Map params) {
        final float width = Float.parseFloat(params.get("width").toString());
        String unitId = params.get("unitId").toString();
        AdSdk.getInstance().loadInterstitialAd(mActivity, unitId, width, new AdSdk.InterstitialAdListener() {
            @Override
            public void onAdLoad(String id) {
                Log.d(TAG, "InterstitialAd onAdLoad");

                sendStatus("onAdLoad", id);
            }

            @Override
            public void onAdShow(String id) {
                Log.d(TAG, "InterstitialAd onAdShow");

                sendStatus("onAdShow", id);
            }

            @Override
            public void onAdClose(String id) {
                Log.d(TAG, "InterstitialAd onAdClose");

                sendStatus("onAdClose", id);
            }

            @Override
            public void onAdClick(String id) {
                Log.d(TAG, "InterstitialAd onAdClick");

                sendStatus("onAdClick", id);
            }

            @Override
            public void onError(String id, int code, String message) {
                Log.d(TAG, "InterstitialAd onError: code=" + code + ", message=" + message);

                sendError( id, code, message);
            }
        });
    }
}
