package cn.bloomad.module;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.linkin.adsdk.AdSdk;

import java.util.Map;

public class BannerModule extends EventModule {
    private AdSdk.BannerAd mBannerAd;

    public BannerModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
    }

    @Override
    public void threadAction(Activity mActivity, Map params) {
        ViewGroup viewGroup = (ViewGroup) params.get("viewGroup");
        Double width = (double) params.get("width");
        Double height = (double) params.get("height");
        String unitId = params.get("unitId").toString();
        AdSdk.getInstance().loadBannerAd(mActivity, unitId, viewGroup, width.floatValue(), height.floatValue(),
                new AdSdk.BannerAdListener() {
                    @Override
                    public void onAdLoad(String id, AdSdk.BannerAd ad) {
                        Log.d(TAG, "BannerAd onAdLoad");

                        // 设置刷新频率，为0或30~120之间的整数，单位秒，0表示不自动轮播，默认30秒
                        ad.setRefreshInterval(30);
                        mBannerAd = ad;
                        sendStatus("onAdLoad", id);
                    }

                    @Override
                    public void onAdShow(String id) {
                        Log.d(TAG, "BannerAd onAdShow");
                        sendStatus("onAdShow", id);
                    }

                    @Override
                    public void onAdClose(String id) {
                        Log.d(TAG, "BannerAd onAdClose");
                        sendStatus("onAdClose", id);
                    }

                    @Override
                    public void onAdClick(String id) {
                        Log.d(TAG, "BannerAd onAdClick");
                        sendStatus("onAdClick", id);
                    }

                    @Override
                    public void onError(String id, int code, String message) {
                        Log.d(TAG, "BannerAd onError: code=" + code + ", message=" + message);
                        sendError(id, code, message);
                    }
                });
    }

    @Override
    public void sendEvent(WritableMap event) {
        Log.d(TAG, "sendEvent:" + event.toString());
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }

    @Override
    public void destroy() {
        if (null != mBannerAd) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "destroy");
                    mBannerAd.destroy();
                    mBannerAd = null;
                }
            });

        }
    }
}
